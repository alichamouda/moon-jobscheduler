/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.moon.jobscheduler._moonquartz.jobs;

import com.moon.jobscheduler.services.AsyncKafkaMessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is an implementation of {@link Job} that executes when triggered to send
 * emails. This job retrieves the list of emails, cc and bcc from the
 * {@code JobDetail} {@code JobDataMap} and passes them to the
 * {@code JavaMailSender} for SMTP transport.
 * <p>
 * There setter methods defined that correspond to the keys stored in the
 * JobDateMap that the {@code JobFactory} implementation will set when
 * instantiating this Job.
 *
 * @author Julius Krah
 * @since September 2017
 */
@Slf4j
@Setter
public class KafkaSenderJob implements Job {

    private String topicName;
    private String key;
    private String message;
    Logger logger = LogManager.getLogger(KafkaSenderJob.class);
    private final AsyncKafkaMessageSender asyncKafkaMessageSender;

    @Autowired
    public KafkaSenderJob(AsyncKafkaMessageSender asyncKafkaMessageSender) {
        this.asyncKafkaMessageSender = asyncKafkaMessageSender;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Job triggered to send Kafka message ...");
        asyncKafkaMessageSender.sendKafkaMessage(topicName, key, message);
        logger.info("Job completed");
    }


}
