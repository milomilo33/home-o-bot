package com.robot.homeobot.repository;

import com.robot.homeobot.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Alarm findByDescriptionAndType(String description, Alarm.AlarmType type);
}
