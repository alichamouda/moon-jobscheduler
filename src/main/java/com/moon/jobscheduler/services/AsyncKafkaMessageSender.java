package com.moon.jobscheduler.services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
public class AsyncKafkaMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    Logger logger = LogManager.getLogger(AsyncKafkaMessageSender.class);

    @Autowired
    public AsyncKafkaMessageSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendKafkaMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message).addCallback(
              new ListenableFutureCallback<SendResult<String, String>>() {
                  @Override
                  public void onSuccess(SendResult<String, String> stringStringSendResult) {
                  }
                  @Override
                    public void onFailure(Throwable throwable) {
                        logger.error(String.format("Error sending kafka message topic:%s key:%s message:%s", topic, key, message));
                    }
                }
        );
    }
}
