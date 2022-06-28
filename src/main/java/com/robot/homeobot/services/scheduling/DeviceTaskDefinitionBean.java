package com.robot.homeobot.services.scheduling;

import com.robot.homeobot.model.Device;
import com.robot.homeobot.repository.DeviceRepository;
import com.robot.homeobot.services.pki.CertificateService;
import com.robot.homeobot.util.Base64Utility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Override
    public void run() {
        // read messages for this device, verify them, apply filter and save them
        System.out.println("read messages for this device, verify them, apply filter and save them - " + device.getName());

        List<String> messages = new ArrayList<>();
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
                addMessageIfFilter(message," (MESSAGE IS NOT DIGITALLY SIGNED!)", messages, device.getFilter());
                iter += 2;
                continue;
            }

            try {
                String status = certificateService.getCertificateStatusAndVerify(device.getName());
                if (!status.equals("Valid")) {
                    addMessageIfFilter(message," (CERTIFICATE IS NOT VALID!)", messages, device.getFilter());
                    iter += 2;
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                addMessageIfFilter(message, String.format(" (%s)", e.getMessage()), messages, device.getFilter());
                iter += 2;
                continue;
            }

            PublicKey devicePublicKey = certificateService.getPublicKeyFromCertificate(device.getName());
            byte[] signatureBytes;
            try {
                signatureBytes = Base64Utility.decode(signature);
            } catch (IOException e) {
                e.printStackTrace();
                addMessageIfFilter(message, " (INCORRECT DIGITAL SIGNATURE FORMAT!)", messages, device.getFilter());
                iter += 2;
                continue;
            }
            boolean signatureValid = verifySignature(message.getBytes(), signatureBytes, devicePublicKey);
            if (!signatureValid) {
                addMessageIfFilter(message, " (DIGITAL SIGNATURE INVALID!)", messages, device.getFilter());
                iter += 2;
                continue;
            }

            addMessageIfFilter(message, "", messages, device.getFilter());

            iter += 2;
        }

        for (String m : messages) {
            System.out.println(m);
        }

        device.setMessages(messages);
        deviceRepository.save(device);

        System.out.println("DONE - " + device.getName());
    }

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

    private void addMessageIfFilter(String message, String addendum, List<String> messages, String filter) {
        if (filter == null) {
            messages.add(message + addendum);
            return;
        }

        boolean match = Pattern.matches(filter, message);
        System.out.println(filter + " " + message + " " + match);
        if (match || filter.equals("")) {
            messages.add(message + addendum);
        }
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
