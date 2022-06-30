package com.robot.homeobot.controller;

import com.robot.homeobot.model.Alarm;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.User;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogsController {
    private final Javers javers;

    public LogsController(Javers javers) {
        this.javers = javers;
    }

    @GetMapping("/device/snapshots")
    @PreAuthorize("hasRole('ADMIN')")
    public String getStoresSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Device.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }
    @GetMapping("/user/snapshots")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsersSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(User.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }
    @GetMapping("/real-estate/snapshots")
    @PreAuthorize("hasRole('ADMIN')")
    public String getRealEstateSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(RealEstate.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }
    @GetMapping("/alarm/snapshots")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAlarmSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Alarm.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

    @GetMapping("/all/snapshots")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Alarm.class);
        List<CdoSnapshot> snapshots1 = javers.findSnapshots(jqlQuery.build());
        jqlQuery = QueryBuilder.byClass(RealEstate.class);
        List<CdoSnapshot> snapshots2 = javers.findSnapshots(jqlQuery.build());
        jqlQuery = QueryBuilder.byClass(User.class);
        List<CdoSnapshot> snapshots3 = javers.findSnapshots(jqlQuery.build());
        jqlQuery = QueryBuilder.byClass(Device.class);
        List<CdoSnapshot> snapshots4 = javers.findSnapshots(jqlQuery.build());
        List<CdoSnapshot> allSnapshots = new ArrayList<>();
        allSnapshots.addAll(snapshots1);
        allSnapshots.addAll(snapshots2);
        allSnapshots.addAll(snapshots3);
        allSnapshots.addAll(snapshots4);
        return javers.getJsonConverter().toJson(allSnapshots);
    }
}
