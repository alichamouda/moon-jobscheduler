package com.moon.jobscheduler._moonquartz.dtos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.quartz.JobBuilder.*;

import com.moon.jobscheduler.dtos.ScheduleRequest;
import com.moon.jobscheduler.dtos.ScheduleRequestTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moon.jobscheduler._moonquartz.jobs.KafkaSenderJob;

import lombok.Data;

@Data
public class JobDescriptor {

    private String topicName;
    private String key;
    private String message;
    private String name;
    private String group;

    private Map<String, Object> data = new LinkedHashMap<>();

    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    public JobDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public JobDescriptor setKey(String key) {
        this.key = key;
        return this;
    }

    public JobDescriptor setMessage(String message) {
        this.message = message;
        return this;
    }


    public JobDescriptor setTopicName(final String topicName) {
        this.topicName = topicName;
        return this;
    }

    public JobDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }


    public JobDescriptor setData(final Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
        return this;
    }

    /**
     * Convenience method for building Triggers of Job
     *
     * @return Triggers for this JobDetail
     */
    @JsonIgnore
    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }

        return triggers;
    }

    /**
     * Convenience method that builds a JobDetail
     *
     * @return the JobDetail built from this descriptor
     */
    public JobDetail buildJobDetail() {
        JobDataMap jobDataMap = new JobDataMap(getData());
        jobDataMap.put("topicName", getTopicName());
        jobDataMap.put("key", getKey());
        jobDataMap.put("message", getMessage());

        return newJob(KafkaSenderJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(jobDataMap)
                .build();
    }

    /**
     * Convenience method that builds a descriptor from JobDetail and Trigger(s)
     *
     * @param jobDetail     the JobDetail instance
     * @param triggersOfJob the Trigger(s) to associate with the Job
     * @return the JobDescriptor
     */

    public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {

        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (Trigger trigger : triggersOfJob) {
            triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
        }

        return new JobDescriptor()
                .setName(jobDetail.getKey().getName())
                .setGroup(jobDetail.getKey().getGroup())
                .setMessage(jobDetail.getJobDataMap().getString("message"))
                .setKey(jobDetail.getJobDataMap().getString("key"))
                .setTopicName(jobDetail.getJobDataMap().getString("topicName"))
                .setTriggerDescriptors(triggerDescriptors);

    }

    public static JobDescriptor buildDescriptor(ScheduleRequest scheduleRequest) {

        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (ScheduleRequestTrigger requestTrigger : scheduleRequest.getTriggers()) {
            triggerDescriptors.add(
                    TriggerDescriptor.buildDescriptor(
                            scheduleRequest.getName(),
                            scheduleRequest.getServiceName(),
                            requestTrigger.getCronTrigger(),
                            requestTrigger.getDateTrigger()));
        }

        return new JobDescriptor()
                .setName(scheduleRequest.getName())
                .setGroup(scheduleRequest.getServiceName())
                .setTopicName(scheduleRequest.getTopicName())
                .setMessage(scheduleRequest.getMessage())
                .setKey(scheduleRequest.getKey())
                .setTriggerDescriptors(triggerDescriptors);

    }
}
