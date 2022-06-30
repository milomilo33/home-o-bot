package com.robot.homeobot.repository;

import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.AlarmTriggered;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface AlarmTriggeredRepository extends JpaRepository<AlarmTriggered, Long> {
}
