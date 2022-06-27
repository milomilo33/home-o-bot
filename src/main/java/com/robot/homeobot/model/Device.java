package com.robot.homeobot.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Device {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;
    private LocalTime period;
    private String filter;

    @ManyToOne(fetch = FetchType.EAGER)
    private RealEstate realEstate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> messages;

    public Device() {}

    public Device(Long id, String name, String path, LocalTime period, String filter, RealEstate realEstate, List<String> messages) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.period = period;
        this.filter = filter;
        this.realEstate = realEstate;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
