package com.example.notificationservice.services.sms;

import com.example.notificationservice.models.requestbody.SmsRequestBody;
import com.example.notificationservice.models.responsebody.SmsResponseBody;
import com.example.notificationservice.models.SMS;
import com.example.notificationservice.services.thirdparty.ThirdPartySenderService;

import com.example.notificationservice.services.kafka.KafkaProducerService;
import com.example.notificationservice.repositories.SmsRepository;
import com.example.notificationservice.utils.phoneNumberValidator;
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
    private  final ThirdPartySenderService thirdPartySendService;

    public SmsResponseBody publishToKafka(SmsRequestBody smsRequestBody) throws Exception {
        String phoneNumber = smsRequestBody.getPhoneNumber();
        String message = smsRequestBody.getMessage();


        if (phoneNumberValidator.isValidNumber(phoneNumber) == false)
            throw new Exception("Cannot send sms to invalid number.");
        if (message.length() > 200)
            throw new Exception("message length must be less than 200 characters.");
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
        String desc ="";
        try {
            desc  =  thirdPartySendService.send(smsRecord);
        } catch (ResourceAccessException e) {
            smsRecord.setStatus("FAILED");
            smsRecord.setFailureComments("External API took too much time to respond");
            smsRecord.setFailureCode(404);
            smsRecord.setUpdatedAt(LocalDateTime.now());
            smsRepository.save(smsRecord);
            return;
        }

        if (desc.equals("Queued")) {
            smsRecord.setStatus("SUCCESS");
            smsRecord.setUpdatedAt(LocalDateTime.now());
            smsRepository.save(smsRecord);
        } else {
            smsRecord.setStatus("FAILED");
            smsRecord.setFailureCode(404);
            smsRecord.setFailureComments(desc);
            smsRecord.setUpdatedAt(LocalDateTime.now());
            smsRepository.save(smsRecord);
        }
    }

    public SmsResponseBody getById(String id) throws Exception, InternalError {
        boolean exists;
        try {
            exists = smsRepository.existsById(id);
        } catch (DataAccessException e) {
            throw new InternalError("Internal Server Error");
        }

        if (!exists) {
            throw new Exception("The given request id is not present in the DataBase");
        }

        return SmsResponseBody.builder().data(Stream.of(smsRepository.findById(id).get()).collect(Collectors.toList())).build();
    }

}
