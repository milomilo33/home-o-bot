package com.robot.homeobot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AlarmTriggered {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String valueTriggeredOn;
    private LocalDateTime dateTriggered;

    @ManyToOne
    private Device deviceTriggeredOn;

    public Device getDeviceTriggeredOn() {
        return deviceTriggeredOn;
    }

    public void setDeviceTriggeredOn(Device deviceTriggeredOn) {
        this.deviceTriggeredOn = deviceTriggeredOn;
    }

    public AlarmTriggered() {}

    public AlarmTriggered(Long id, String valueTriggeredOn, LocalDateTime dateTriggered, Device deviceTriggeredOn) {
        this.id = id;
        this.valueTriggeredOn = valueTriggeredOn;
        this.dateTriggered = dateTriggered;
        this.deviceTriggeredOn = deviceTriggeredOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValueTriggeredOn() {
        return valueTriggeredOn;
    }

    public void setValueTriggeredOn(String valueTriggeredOn) {
        this.valueTriggeredOn = valueTriggeredOn;
    }

    public LocalDateTime getDateTriggered() {
        return dateTriggered;
    }

    public void setDateTriggered(LocalDateTime dateTriggered) {
        this.dateTriggered = dateTriggered;
    }
}
