package com.robot.homeobot.repository;

import com.robot.homeobot.model.Device;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Device findByName(String name);

}
