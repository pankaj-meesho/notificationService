package com.example.notificationservice.services.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Value("${KAFKA_TOPIC}")
    private String topic;

    private static final Logger LOGGER = LogManager.getLogger(KafkaProducerService.class);

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate <String, String> kafkaTemp;

    public void publishToTopic(String message){
        LOGGER.info("Publishing to topic {}",topic);
        this.kafkaTemp.send(topic,message);
    }
}
