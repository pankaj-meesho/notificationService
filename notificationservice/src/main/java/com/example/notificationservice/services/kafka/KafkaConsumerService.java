package com.example.notificationservice.services.kafka;

import com.example.notificationservice.models.SMS;
import com.example.notificationservice.services.sms.SmsService;
import com.example.notificationservice.repositories.SmsRepository;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class KafkaConsumerService {
    private final SmsRepository smsRepository;
    private final SmsService smsService;

    private static final Logger LOGGER = LogManager.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "${KAFKA_TOPIC}", groupId = "${KAFKA_CONSUMER_GROUP_ID}")
    public void consumeFromTopic(String id) {
        LOGGER.info("Consumed message with the id => {}", id);
        SMS sms = smsRepository.findById(id).get();

        String phoneNumber = sms.getPhoneNumber();
     smsService.thirdPartySmsHandler(sms);
    }
    }
