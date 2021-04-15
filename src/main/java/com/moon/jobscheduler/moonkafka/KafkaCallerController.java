package com.moon.jobscheduler.moonkafka;

import com.moon.jobscheduler._moonquartz.dtos.JobDescriptor;
import com.moon.jobscheduler._moonquartz.services.JobService;
import com.moon.jobscheduler.dtos.ScheduleRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaCallerController {


    private final JobService jobService;
    Logger logger = LogManager.getLogger(KafkaCallerController.class);

    @Autowired
    public KafkaCallerController(JobService jobService) {
        this.jobService = jobService;
    }

    @KafkaListener(topics = "${create-job-topic}", groupId = "${create-job-consumer-group}",
            containerFactory = " scheduleRequestKafkaListenerContainerFactory")
    public void createJob(@Payload ScheduleRequest scheduleRequest) {
        logger.info("Scheduler has been called. Request object is:");
        logger.info(scheduleRequest);
        JobDescriptor jobDescriptor = jobService.createJob(scheduleRequest.getServiceName(), JobDescriptor.buildDescriptor(scheduleRequest));
        logger.info("Job Created: ");
        logger.info(jobDescriptor);
    }

}
