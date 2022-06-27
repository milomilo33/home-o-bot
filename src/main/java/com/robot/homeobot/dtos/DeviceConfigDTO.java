package com.robot.homeobot.dtos;

import com.robot.homeobot.model.Device;

import java.time.LocalTime;

public class DeviceConfigDTO {
    private Long id;
    private String path;
    private LocalTime period;
    private String filter;

    public DeviceConfigDTO() {}

    public DeviceConfigDTO(Long id, String path, LocalTime period, String filter) {
        this.id = id;
        this.path = path;
        this.period = period;
        this.filter = filter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalTime period) {
        this.period = period;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
