package com.robot.homeobot.dtos;

public class CSRDTO {
    public String CN;
    public String OU;
    public String O;
    public String L;
    public String ST;
    public String C;

    public CSRDTO() {}

    public CSRDTO(String CN, String OU, String O, String L, String ST, String C) {
        this.CN = CN;
        this.OU = OU;
        this.O = O;
        this.L = L;
        this.ST = ST;
        this.C = C;
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
}
