package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.repository.DeviceRepository;
import com.robot.homeobot.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

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

    @Override
    @Transactional
    public List<Device> updateAllDevicesConfig(List<DeviceConfigDTO> dtos) {
        for (DeviceConfigDTO dto : dtos) {
            Device device = this.findById(dto.getId());
            device.setPath(dto.getPath());
            device.setPeriod(dto.getPeriod());
            device.setFilter(dto.getFilter());
        }

        return deviceRepository.findAll();
    }

}
