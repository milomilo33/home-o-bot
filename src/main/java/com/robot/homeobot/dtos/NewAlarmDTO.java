package com.robot.homeobot.dtos;

public class NewAlarmDTO {

    private String description;
    private String deviceName;
    private String triggerPattern;

    public NewAlarmDTO() {}

    public NewAlarmDTO(String description, String deviceName, String triggerPattern) {
        this.description = description;
        this.deviceName = deviceName;
        this.triggerPattern = triggerPattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTriggerPattern() {
        return triggerPattern;
    }

    public void setTriggerPattern(String triggerPattern) {
        this.triggerPattern = triggerPattern;
    }
}
