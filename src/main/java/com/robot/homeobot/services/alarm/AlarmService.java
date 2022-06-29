package com.robot.homeobot.services.alarm;

import com.robot.homeobot.dtos.NewAlarmDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.repository.AlarmRepository;
import com.robot.homeobot.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public Alarm findById(Long id) {
        return alarmRepository.findById(id).orElse(null);
    }

    @Transactional
    public void createNewMessageAlarm(NewAlarmDTO newAlarmDTO) throws MyException {
        Long deviceId = newAlarmDTO.getDeviceId();
        String description = newAlarmDTO.getDescription();
        String triggerPattern = newAlarmDTO.getTriggerPattern();

        if (deviceId == null || description == null || triggerPattern == null) {
            throw new MyException("Invalid input.");
        }

        Device device = deviceRepository.findById(deviceId).orElse(null);

        if (device == null) {
            throw new MyException("Nonexistent device.");
        }

        Alarm alarm = new Alarm();
        alarm.setAllTriggered(new ArrayList<>());
        alarm.setDeviceTrigger(device);
        alarm.setMessageTriggerPattern(triggerPattern);
        alarm.setDescription(description);

        alarmRepository.save(alarm);
    }

}
