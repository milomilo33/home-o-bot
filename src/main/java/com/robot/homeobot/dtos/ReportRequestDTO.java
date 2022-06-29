package com.robot.homeobot.dtos;

import java.time.LocalDateTime;

public class ReportRequestDTO {

    private LocalDateTime start;
    private LocalDateTime end;

    public ReportRequestDTO() {}

    public ReportRequestDTO(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
