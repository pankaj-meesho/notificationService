package com.example.notificationservice.services.sms;

import com.example.notificationservice.models.requestbody.SmsRequestBody;
import com.example.notificationservice.models.responsebody.SmsResponseBody;
import com.example.notificationservice.models.SMS;

import com.example.notificationservice.services.kafka.KafkaProducerService;
import com.example.notificationservice.repositories.SmsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsService {
    private final SmsRepository smsRepository;
    private final KafkaProducerService kafkaProducerService;

    public SmsResponseBody publishToKafka(SmsRequestBody smsRequestBody) throws Exception {
        String phoneNumber = smsRequestBody.getPhoneNumber();
        String message = smsRequestBody.getMessage();


        SMS smsRecord = new SMS();
        smsRecord.setPhoneNumber(phoneNumber);
        smsRecord.setCreatedAt(LocalDateTime.now());
        smsRecord.setMessage(message);
        smsRecord.setStatus("IN_PROCESS");

        UUID randomId = UUID.randomUUID();
        smsRecord.setId(randomId.toString());

        try {
            smsRepository.save(smsRecord);
        } catch (DataAccessException e) {
            throw new InternalError("Internal Server Error");
        }

        String requestId = smsRecord.getId();
        kafkaProducerService.publishToTopic(requestId);
//
//        SmsResponseBody smsResponseBody = new SmsResponseBody();
//        smsResponseBody.setComments("sms is queued, request_id is :" + requestId.toString());
        return SmsResponseBody.builder().comments("sms is queued, request_id is :" + randomId.toString()).build();
    }

    public void thirdPartySmsHandler(SMS smsRecord) {

    }


}
