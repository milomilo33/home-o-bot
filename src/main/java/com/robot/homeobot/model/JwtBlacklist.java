package com.robot.homeobot.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JwtBlacklist {

    @Id
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JwtBlacklist{" +
                ", token='" + token + '\'' +
                '}';
    }
}
