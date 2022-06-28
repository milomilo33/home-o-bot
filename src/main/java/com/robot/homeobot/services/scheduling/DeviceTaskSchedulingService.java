package com.robot.homeobot.services.scheduling;

import com.robot.homeobot.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class DeviceTaskSchedulingService {

    @Autowired
    private TaskScheduler taskScheduler;

    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public void scheduleATask(Runnable tasklet, Device device) {
        System.out.println("Scheduling task for device: " + device.getName() + " at time: " + device.getPeriod());
        int second = device.getPeriod().getSecond(), minute = device.getPeriod().getMinute(), hour = device.getPeriod().getHour();
        CronTrigger trigger = new CronTrigger(String.format("%d %d %d * * ?", second, minute, hour));
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, trigger);
        System.out.println("Trigger: " + trigger);
        jobsMap.put(device.getName(), scheduledTask);
    }

    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(jobId, null);
        }
    }

}
