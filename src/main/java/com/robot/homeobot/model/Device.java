package com.robot.homeobot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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
    @JsonBackReference
    private RealEstate realEstate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> messages;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> messageTypes;

    public List<String> getMessageTypes() {
        return messageTypes;
    }

    public void setMessageTypes(List<String> messageTypes) {
        this.messageTypes = messageTypes;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Device device = (Device) o;
//        return Objects.equals(id, device.id) &&
//                Objects.equals(name, device.name) &&
//                Objects.equals(path, device.path) &&
//                Objects.equals(period, device.period) &&
//                Objects.equals(filter, device.filter) &&
//                Objects.equals(realEstate, device.realEstate) &&
//                Objects.equals(messages, device.messages) &&
//                Objects.equals(messageTypes, device.messageTypes);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, path, period, filter, realEstate, messages, messageTypes);
//    }

    public Device() {}

    public Device(Long id, String name, String path, LocalTime period, String filter, RealEstate realEstate, List<String> messages, List<String> messageTypes) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.period = period;
        this.filter = filter;
        this.realEstate = realEstate;
        this.messages = messages;
        this.messageTypes = messageTypes;
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
