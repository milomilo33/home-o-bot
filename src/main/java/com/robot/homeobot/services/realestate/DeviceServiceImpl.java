package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.repository.DeviceRepository;
import com.robot.homeobot.repository.RealEstateRepository;
import com.robot.homeobot.services.scheduling.DeviceTaskDefinitionBean;
import com.robot.homeobot.services.scheduling.DeviceTaskSchedulingService;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private DeviceTaskSchedulingService deviceTaskSchedulingService;

    @Override
    public Device findById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Device> findAll() {
        List<Device> devices = deviceRepository.findAll();
        if (devices.isEmpty()) {
            List<String> lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get("devices/conf.txt"), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String deviceName : lines) {
                Device device = new Device();
                device.setName(deviceName);

                List<RealEstate> allRealEstate = realEstateRepository.findAll();
                for (RealEstate re : allRealEstate) {
                    if (re.getDeviceNames().contains(deviceName)) {
                        device.setRealEstate(re);
                        break;
                    }
                }

                deviceRepository.save(device);
            }

            devices = deviceRepository.findAll();
        }

        return devices;
    }

    @Autowired
    private ObjectFactory<DeviceTaskDefinitionBean> myBeanFactory;

    @Override
    @Transactional
    public List<Device> updateAllDevicesConfig(List<DeviceConfigDTO> dtos) {
        for (DeviceConfigDTO dto : dtos) {
            Device device = this.findById(dto.getId());
            device.setPath(dto.getPath());
            device.setPeriod(dto.getPeriod());
            device.setFilter(dto.getFilter());

            if (device.getPeriod() != null) {
                deviceTaskSchedulingService.removeScheduledTask(device.getName());

                DeviceTaskDefinitionBean deviceTaskDefinitionBean = myBeanFactory.getObject();
                deviceTaskDefinitionBean.setDevice(device);
                deviceTaskSchedulingService.scheduleATask(deviceTaskDefinitionBean, device);
            }
        }

        return deviceRepository.findAll();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public DeviceTaskDefinitionBean createDeviceTaskDefinitionBean() {
        return new DeviceTaskDefinitionBean();
    }

    @Override
    public List<String> getAllDeviceNames() {
        return deviceRepository.findAll().stream().map(Device::getName).collect(Collectors.toList());
    }

//    private final KieContainer kieContainer;
//
//    @Autowired
//    public DeviceServiceImpl(KieContainer kieContainer) {
//        this.kieContainer = kieContainer;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void test() {
//        KieSession kieSession = kieContainer.newKieSession("alarmRulesSession");
//
////        kieSession.insert(user);
//        int fired = kieSession.fireAllRules();
//        System.out.println("Number of Rules executed = " + fired);
//        kieSession.dispose();
//    }
}
