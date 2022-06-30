package com.robot.homeobot.services.alarm;

import com.robot.homeobot.dtos.NewAlarmDTO;
import com.robot.homeobot.dtos.ReportRequestDTO;
import com.robot.homeobot.dtos.ReportResponseDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.AlarmTriggered;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.AlarmRepository;
import com.robot.homeobot.repository.DeviceRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        String deviceName = newAlarmDTO.getDeviceName();
        String description = newAlarmDTO.getDescription();
        String triggerPattern = newAlarmDTO.getTriggerPattern();

        if (deviceName == null || description == null || triggerPattern == null) {
            throw new MyException("Invalid input.");
        }

        Device device = deviceRepository.findByName(deviceName);

        if (device == null) {
            throw new MyException("Nonexistent device.");
        }

        Alarm alarm = new Alarm();
        alarm.setAllTriggered(new ArrayList<>());
        alarm.setDeviceTrigger(device);
        alarm.setMessageTriggerPattern(triggerPattern);
        alarm.setDescription(description);
        alarm.setType(Alarm.AlarmType.MESSAGE);

        alarmRepository.save(alarm);
    }

    public List<ReportResponseDTO> getAlarmReport(ReportRequestDTO dto) throws MyException {
        LocalDateTime start = dto.getStart();
        LocalDateTime end = dto.getEnd();

        if (start == null || end == null) {
            throw new MyException("Invalid input.");
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ReportResponseDTO> response = new ArrayList<>();

        List<Device> devices = deviceRepository.findAll();

        for (Device d : devices) {
            if (currentUser.getRoles().get(0).getName().equals("ROLE_OWNER")) {
                if (d.getRealEstate().getOwner() != null && d.getRealEstate().getOwner().getId().equals(currentUser.getId())) {
                    Integer counter = 0;
                    List<Alarm> alarms = alarmRepository.findAll();
                    for (Alarm a : alarms) {
                        if (a.getType() == Alarm.AlarmType.LOG) {
                            continue;
                        }
                        for (AlarmTriggered at : a.getAllTriggered()) {
                            if (at.getDeviceTriggeredOn() != null && at.getDeviceTriggeredOn().getId().equals(d.getId())) {
                                LocalDateTime triggeredAt = at.getDateTriggered();
                                if (triggeredAt.isBefore(end) && triggeredAt.isAfter(start)) {
                                    counter++;
                                }
                            }
                        }
                    }

                    ReportResponseDTO devResponse = new ReportResponseDTO(d.getName(), d.getRealEstate().getName(), counter);
                    response.add(devResponse);
                }
            }
            else if (currentUser.getRoles().get(0).getName().equals("ROLE_RENTER")) {
                if (d.getRealEstate().getRenters().stream().anyMatch(r -> r.getId().equals(currentUser.getId()))) {
                    Integer counter = 0;
                    List<Alarm> alarms = alarmRepository.findAll();
                    for (Alarm a : alarms) {
                        if (a.getType() == Alarm.AlarmType.LOG) {
                            continue;
                        }
                        for (AlarmTriggered at : a.getAllTriggered()) {
                            if (at.getDeviceTriggeredOn() != null && at.getDeviceTriggeredOn().getId().equals(d.getId())) {
                                LocalDateTime triggeredAt = at.getDateTriggered();
                                if (triggeredAt.isBefore(end) && triggeredAt.isAfter(start)) {
                                    counter++;
                                }
                            }
                        }
                    }

                    ReportResponseDTO devResponse = new ReportResponseDTO(d.getName(), d.getRealEstate().getName(), counter);
                    response.add(devResponse);
                }
            }
        }

        return response;
    }

}
