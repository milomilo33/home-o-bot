package com.robot.homeobot.dtos;

public class ReportResponseDTO {

    private String deviceName;
    private String realEstateName;
    private Integer numOfTriggeredAlarms;

    public ReportResponseDTO() {}

    public ReportResponseDTO(String deviceName, String realEstateName, Integer numOfTriggeredAlarms) {
        this.deviceName = deviceName;
        this.realEstateName = realEstateName;
        this.numOfTriggeredAlarms = numOfTriggeredAlarms;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRealEstateName() {
        return realEstateName;
    }

    public void setRealEstateName(String realEstateName) {
        this.realEstateName = realEstateName;
    }

    public Integer getNumOfTriggeredAlarms() {
        return numOfTriggeredAlarms;
    }

    public void setNumOfTriggeredAlarms(Integer numOfTriggeredAlarms) {
        this.numOfTriggeredAlarms = numOfTriggeredAlarms;
    }
}
