package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.model.Device;

import java.util.List;

public interface DeviceService {
    Device findById(Long id);
    List<Device> findAll();
    List<Device> updateAllDevicesConfig(List<DeviceConfigDTO> dtos);
}
