package com.robot.homeobot.dtos;

import java.time.LocalDateTime;

public class AllCertificatesDTO {
    public String CN;
    public String OU;
    public String O;
    public String L;
    public String ST;
    public String C;
    public LocalDateTime start;
    public LocalDateTime end;
    public String status;

    public AllCertificatesDTO() {}

    public AllCertificatesDTO(String CN, String OU, String o, String l, String ST, String c, LocalDateTime start, LocalDateTime end, String status) {
        this.CN = CN;
        this.OU = OU;
        O = o;
        L = l;
        this.ST = ST;
        C = c;
        this.start = start;
        this.end = end;
        this.status = status;
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

    public void setST(String ST) {
        this.ST = ST;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
