package com.robot.homeobot.repository;

import com.robot.homeobot.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
