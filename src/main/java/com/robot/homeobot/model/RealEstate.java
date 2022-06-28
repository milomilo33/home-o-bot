package com.robot.homeobot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class RealEstate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<String> deviceNames;

    @OneToMany(mappedBy = "realEstate", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonManagedReference
    private Set<Device> devices;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<String> alarmValues;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "real_estate_renters",
//            joinColumns = @JoinColumn(name = "real_estate_id",
//                    referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "renter_id", referencedColumnName = "id"))
    @ManyToMany(mappedBy = "rentedRealEstate", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<User> renters;

    public RealEstate() {}

    public RealEstate(Long id, String name, Set<String> deviceNames, Set<Device> devices, Set<String> alarmValues, User owner, Set<User> renters) {
        this.id = id;
        this.name = name;
        this.deviceNames = deviceNames;
        this.devices = devices;
        this.alarmValues = alarmValues;
        this.owner = owner;
        this.renters = renters;
    }

    public Set<User> getRenters() {
        return renters;
    }

    public void setRenters(Set<User> renters) {
        this.renters = renters;
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

    public Set<String> getDeviceNames() {
        return deviceNames;
    }

    public void setDeviceNames(Set<String> deviceNames) {
        this.deviceNames = deviceNames;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<String> getAlarmValues() {
        return alarmValues;
    }

    public void setAlarmValues(Set<String> alarmValues) {
        this.alarmValues = alarmValues;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
