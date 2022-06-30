package com.robot.homeobot.repository;

import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.Device;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@JaversSpringDataAuditable
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Alarm findByDescriptionAndType(String description, Alarm.AlarmType type);
    List<Alarm> findAlarmsByTypeAndDeviceTrigger(Alarm.AlarmType type, Device device);
}
