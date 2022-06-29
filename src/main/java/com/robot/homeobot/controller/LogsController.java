package com.robot.homeobot.controller;

import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.User;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;

import java.util.List;

@RestController
public class LogsController {
    private final Javers javers;

    public LogsController(Javers javers) {
        this.javers = javers;
    }

    @GetMapping("/device/snapshots")
    public String getStoresSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Device.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }
    @GetMapping("/user/snapshots")
    public String getUsersSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(User.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }
}
