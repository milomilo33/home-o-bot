package com.robot.homeobot.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "alarms")
public class Alarm {

    public enum AlarmType {
        LOG, MESSAGE, PREDEFINED
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    private String messageTriggerPattern;
    @ManyToOne(fetch = FetchType.EAGER)
    private Device deviceTrigger;

    private String logTriggerType;
    private String logTriggerValue;
    private LocalDateTime logTriggerDate;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<AlarmTriggered> allTriggered = new ArrayList<>();

    public Alarm() {}

    public Alarm(Long id, String description, AlarmType type, String messageTriggerPattern, Device deviceTrigger, String logTriggerType, String logTriggerValue, LocalDateTime logTriggerDate, List<AlarmTriggered> allTriggered) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.messageTriggerPattern = messageTriggerPattern;
        this.deviceTrigger = deviceTrigger;
        this.logTriggerType = logTriggerType;
        this.logTriggerValue = logTriggerValue;
        this.logTriggerDate = logTriggerDate;
        this.allTriggered = allTriggered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlarmType getType() {
        return type;
    }

    public void setType(AlarmType type) {
        this.type = type;
    }

    public String getMessageTriggerPattern() {
        return messageTriggerPattern;
    }

    public void setMessageTriggerPattern(String messageTriggerPattern) {
        this.messageTriggerPattern = messageTriggerPattern;
    }

    public Device getDeviceTrigger() {
        return deviceTrigger;
    }

    public void setDeviceTrigger(Device deviceTrigger) {
        this.deviceTrigger = deviceTrigger;
    }

    public String getLogTriggerType() {
        return logTriggerType;
    }

    public void setLogTriggerType(String logTriggerType) {
        this.logTriggerType = logTriggerType;
    }

    public String getLogTriggerValue() {
        return logTriggerValue;
    }

    public void setLogTriggerValue(String logTriggerValue) {
        this.logTriggerValue = logTriggerValue;
    }

    public LocalDateTime getLogTriggerDate() {
        return logTriggerDate;
    }

    public void setLogTriggerDate(LocalDateTime logTriggerDate) {
        this.logTriggerDate = logTriggerDate;
    }

    public List<AlarmTriggered> getAllTriggered() {
        return allTriggered;
    }

    public void setAllTriggered(List<AlarmTriggered> allTriggered) {
        this.allTriggered = allTriggered;
    }
}
