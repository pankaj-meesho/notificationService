package com.example.notificationservice.controllers;

import  com.example.notificationservice.models.responsebody.SmsResponseBody;
import  com.example.notificationservice.models.requestbody.SmsRequestBody;
import com.example.notificationservice.services.sms.SmsService;
import  com.example.notificationservice.models.SMS;
import  com.example.notificationservice.repositories.SmsRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor(onConstructor_=@Autowired)
public class SmsController {

    private final SmsService smsService;
    private final SmsRepository smsRepository;
    private static final Logger LOGGER = LogManager.getLogger(SmsController.class);

    @GetMapping
    public List<SMS> getAllSMS(){
        return smsRepository.findAll();
    }

    @PostMapping
    public SMS createSMS(@RequestBody SMS sms) {
        return smsRepository.save(sms);
    }

    @PostMapping(path = "/sms/send")
    public ResponseEntity<SmsResponseBody> sendSms(@RequestBody SmsRequestBody smsRequestBody) {
        try {
            return ResponseEntity.ok(smsService.publishToKafka(smsRequestBody));
        } catch (Exception e) {
            LOGGER.error("Failed Validation Checks");
            return ResponseEntity.badRequest().body(SmsResponseBody.builder().error(e.getMessage()).build());
        }

    }
}
