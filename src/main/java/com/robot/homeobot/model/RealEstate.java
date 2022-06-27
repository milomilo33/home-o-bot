package com.robot.homeobot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealEstate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<String> deviceNames;

    @OneToMany(mappedBy = "realEstate")
    Set<Device> devices;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<String> alarmValues;

    @ManyToOne
    User owner;

}
