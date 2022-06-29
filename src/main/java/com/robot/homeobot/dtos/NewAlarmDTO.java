package com.robot.homeobot.dtos;

public class NewAlarmDTO {

    private String description;
    private Long deviceId;
    private String triggerPattern;

    public NewAlarmDTO() {}

    public NewAlarmDTO(String description, Long deviceId, String triggerPattern) {
        this.description = description;
        this.deviceId = deviceId;
        this.triggerPattern = triggerPattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getTriggerPattern() {
        return triggerPattern;
    }

    public void setTriggerPattern(String triggerPattern) {
        this.triggerPattern = triggerPattern;
    }
}
