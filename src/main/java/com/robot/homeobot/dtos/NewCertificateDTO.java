package com.robot.homeobot.dtos;

import java.time.LocalDateTime;

public class NewCertificateDTO {
    private String CN;
    private String OU;
    private String O;
    private String L;
    private String ST;
    private String C;
    private LocalDateTime start;
    private LocalDateTime end;

    public NewCertificateDTO(String CN, String OU, String o, String l, String st, String c, LocalDateTime start, LocalDateTime end) {
        this.CN = CN;
        this.OU = OU;
        O = o;
        L = l;
        ST = st;
        C = c;
        this.start = start;
        this.end = end;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getOU() {
        return OU;
    }

    public void setOU(String OU) {
        this.OU = OU;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getST() {
        return ST;
    }

    public void setST(String st) {
        ST = st;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
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
