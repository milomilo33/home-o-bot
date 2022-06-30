package com.robot.homeobot.services.scheduling;

import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.AlarmTriggered;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.repository.AlarmRepository;
import com.robot.homeobot.repository.AlarmTriggeredRepository;
import com.robot.homeobot.repository.DeviceRepository;
import com.robot.homeobot.services.pki.CertificateService;
import com.robot.homeobot.util.Base64Utility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class DeviceTaskDefinitionBean implements Runnable {

    private Device device;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmTriggeredRepository alarmTriggeredRepository;

    @Override
    public void run() {
        // read messages for this device, verify them, save them and check/trigger alarms
        System.out.println("read messages for this device, verify them, apply filter and save them - " + device.getName());

        List<String> messages = new ArrayList<>();
        List<String> messageTypes = new ArrayList<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(device.getPath()), StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException e) {
            // logovati ?
            e.printStackTrace();
            return;
        }

        int iter = 0;
        while (iter < lines.size()){
            String message = lines.get(iter);
            String signature;
            try {
                signature = lines.get(iter + 1);
            } catch (IndexOutOfBoundsException exc) {
                // nema potpisa; logovati?
                addMessageIfFilter(message," (MESSAGE IS NOT DIGITALLY SIGNED!)", messages, device.getFilter(), messageTypes);
                iter += 2;
                continue;
            }

            try {
                String status = certificateService.getCertificateStatusAndVerify(device.getName());
                if (!status.equals("Valid")) {
                    addMessageIfFilter(message," (CERTIFICATE IS NOT VALID!)", messages, device.getFilter(), messageTypes);
                    iter += 2;
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                addMessageIfFilter(message, String.format(" (%s)", e.getMessage()), messages, device.getFilter(), messageTypes);
                iter += 2;
                continue;
            }

            PublicKey devicePublicKey = certificateService.getPublicKeyFromCertificate(device.getName());
            byte[] signatureBytes;
            try {
                signatureBytes = Base64Utility.decode(signature);
            } catch (IOException e) {
                e.printStackTrace();
                addMessageIfFilter(message, " (INCORRECT DIGITAL SIGNATURE FORMAT!)", messages, device.getFilter(), messageTypes);
                iter += 2;
                continue;
            }
            boolean signatureValid = verifySignature(message.getBytes(), signatureBytes, devicePublicKey);
            if (!signatureValid) {
                addMessageIfFilter(message, " (DIGITAL SIGNATURE INVALID!)", messages, device.getFilter(), messageTypes);
                iter += 2;
                continue;
            }

            addMessageIfFilter(message, "", messages, device.getFilter(), messageTypes);

            iter += 2;
        }

        for (String m : messages) {
            System.out.println(m);
        }
        for (String m : messageTypes) {
            System.out.println(m);
        }

        device.setMessages(messages);
        device.setMessageTypes(messageTypes);
        deviceRepository.save(device);

        // check if messages trigger alarms
        Alarm dangerAlarm = alarmRepository.findByDescriptionAndType("Danger device messages alarm", Alarm.AlarmType.PREDEFINED);
        int triggeredAlarmsSnapshot = dangerAlarm.getAllTriggered().size();
        KieSession kieSession = kieContainer.newKieSession("alarmRulesSession");

//        device.setRealEstate(null);
        kieSession.insert(device);
        List<String> messagesWithTypes = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            String messageWithType = messages.get(i) + "|" + messageTypes.get(i);
            messagesWithTypes.add(messageWithType);
        }
        kieSession.insert(messagesWithTypes);
        kieSession.insert(alarmRepository);
        kieSession.insert(alarmTriggeredRepository);
        int fired = kieSession.fireAllRules();
        System.out.println("Number of Rules executed = " + fired);
        kieSession.dispose();

        dangerAlarm = alarmRepository.findByDescriptionAndType("Danger device messages alarm", Alarm.AlarmType.PREDEFINED);
        int triggeredAlarmsAfterRules = dangerAlarm.getAllTriggered().size();
        if (triggeredAlarmsSnapshot < triggeredAlarmsAfterRules) {
            // notify admin
            for (int i = triggeredAlarmsSnapshot; i < triggeredAlarmsAfterRules; i++) {
                this.template.convertAndSend("/topic/alarms", "New triggered alarm on device " + dangerAlarm.getAllTriggered().get(i).getDeviceTriggeredOn().getName());
            }
        }
//        Alarm alarm = alarmRepository.findByDescriptionAndType("Danger device messages alarm", Alarm.AlarmType.PREDEFINED);

//        InputStream template = DeviceTaskDefinitionBean.class.getResourceAsStream("/alarmTemplateRules/alarmTemplateRules.drl");

//        Alarm alarm = new Alarm(2L, "some desc", Alarm.AlarmType.MESSAGE, ".*!", device, null, null, null, new ArrayList<>());
//        alarmRepository.save(alarm);

        for (Alarm alarm : alarmRepository.findAlarmsByTypeAndDeviceTrigger(Alarm.AlarmType.MESSAGE, device)) {
            triggeredAlarmsSnapshot = alarm.getAllTriggered().size();

            kieSession = kieContainer.newKieSession("alarmTemplateRulesSession");

            kieSession.insert(device);
            kieSession.insert(messages);
            kieSession.insert(alarm);
            kieSession.insert(alarmRepository);
            kieSession.insert(alarmTriggeredRepository);
            fired = kieSession.fireAllRules();
//            System.out.println("Number of Rules executed = " + fired);
//            System.out.println(alarm.getAllTriggered().size());
//            System.out.println(alarm.getAllTriggered().get(0).getDateTriggered());
            kieSession.dispose();

            triggeredAlarmsAfterRules = alarm.getAllTriggered().size();
            if (triggeredAlarmsSnapshot < triggeredAlarmsAfterRules) {
                // notify admin
                for (int i = triggeredAlarmsSnapshot; i < triggeredAlarmsAfterRules; i++) {
                    this.template.convertAndSend("/topic/alarms", "New triggered alarm on device " + dangerAlarm.getAllTriggered().get(i).getDeviceTriggeredOn().getName());
                }
            }
        }

//        ObjectDataCompiler converter = new ObjectDataCompiler();
//        String drl = converter.compile(alarms, template);
//
//        System.out.println(drl);
//
//        KieSession ksession = createKieSessionFromDRL(drl);
//        ksession.insert(1);
//
//        ksession.fireAllRules();
//        ksession.dispose();

        System.out.println("DONE - " + device.getName());
    }

    @Autowired
    private SimpMessagingTemplate template;

    private boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Kreiranje objekta koji nudi funkcionalnost digitalnog potpisivanja
            // Prilikom getInstance poziva prosledjujemo algoritam koji cemo koristiti
            Signature sig = Signature.getInstance("SHA256withRSA/PSS");
            sig.setParameter(new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 20, 1));

            // Navodimo kljuc sa kojim proveravamo potpis
            sig.initVerify(publicKey);

            // Postavljamo podatke koje potpisujemo
            sig.update(data);

            // Vrsimo proveru digitalnog potpisa
            return sig.verify(signature);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addMessageIfFilter(String message, String addendum, List<String> messages, String filter, List<String> messageTypes) {
        if (filter == null) {
            String messageVal = message.split("\\|")[0];
            try {
                String messageType = message.split("\\|")[1];
                if (!messageType.equals("Normal") && !messageType.equals("DANGER")) {
                    messages.add(messageVal + addendum + " (invalid message type!)");
                    messageTypes.add("INVALID_TYPE");
                    return;
                }
                messages.add(messageVal + addendum);
                messageTypes.add(messageType);
                return;
            } catch (IndexOutOfBoundsException exc) {
                messages.add(messageVal + addendum + " (missing message type!)");
                messageTypes.add("NO_TYPE");
                return;
            }
        }

        String messageVal = message.split("\\|")[0];
        boolean match = Pattern.matches(filter, messageVal);
        System.out.println(filter + " " + message + " " + match);
        if (match || filter.equals("")) {
            try {
                String messageType = message.split("\\|")[1];
                if (!messageType.equals("Normal") && !messageType.equals("DANGER")) {
                    messages.add(messageVal + addendum + " (invalid message type!)");
                    messageTypes.add("INVALID_TYPE");
                    return;
                }
                messages.add(messageVal + addendum);
                messageTypes.add(messageType);
            } catch (IndexOutOfBoundsException exc) {
                messages.add(messageVal + addendum + " (missing message type!)");
                messageTypes.add("NO_TYPE");
            }
        }
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private KieSession createKieSessionFromDRL(String drl){
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: "+message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build().newKieSession();
    }
}
