package com.robot.homeobot.controller;


import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.dtos.NewAlarmDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.repository.AlarmRepository;
import com.robot.homeobot.services.alarm.AlarmService;
import com.robot.homeobot.services.realestate.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/alarms", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AlarmRepository alarmRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Alarm>> getAllAlarms() {
        List<Alarm> allAlarms = alarmRepository.findAll();

        return new ResponseEntity<>(allAlarms, HttpStatus.OK);
    }

    @PostMapping("/create-new-message-alarm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewMessageAlarm(@RequestBody NewAlarmDTO dto) {
        try {
            alarmService.createNewMessageAlarm(dto);
        } catch (MyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
