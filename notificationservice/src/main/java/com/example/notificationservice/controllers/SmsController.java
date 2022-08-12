package com.example.notificationservice.controllers;

import  com.example.notificationservice.models.responsebody.SmsResponseBody;
import  com.example.notificationservice.models.requestbody.SmsRequestBody;
import com.example.notificationservice.services.sms.SmsService;
import  com.example.notificationservice.models.SMS;
import  com.example.notificationservice.repositories.SmsRepository;
import com.example.notificationservice.services.blacklist.BlacklistService;
import com.example.notificationservice.models.requestbody.BlacklistRequestBody;
import com.example.notificationservice.models.responsebody.BlacklistResponseBody;


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
    private final BlacklistService blacklistService;

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
    @GetMapping(path = "/sms/{id}")
    public ResponseEntity<SmsResponseBody> getRequestById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(smsService.getById(id));
        } catch (Exception e) {
            LOGGER.error("Input Sms Id not found in DB");
            return ResponseEntity.badRequest().body(SmsResponseBody.builder().error(e.getMessage()).build());
        }
    }
    @GetMapping(path = "/blacklist")
    public ResponseEntity<BlacklistResponseBody> getBlacklist()  {
        try {
            return ResponseEntity.ok(blacklistService.getBlacklist());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BlacklistResponseBody.builder().error(e.getMessage()).build());
        }
    }

    @PostMapping(path = "/blacklist")
    public ResponseEntity<BlacklistResponseBody> addToBlacklist(@RequestBody BlacklistRequestBody requestBody) {
        try {
            return ResponseEntity.ok(blacklistService.addToBlacklist(requestBody.getPhoneNumbers()));
        } catch (Exception e) {
            LOGGER.error("Empty Input");
            return ResponseEntity.badRequest().body(BlacklistResponseBody.builder().error(e.getMessage()).build());
        } catch (InternalError e) {
            LOGGER.error("SQL Query Timed Out");
            return ResponseEntity.internalServerError().body(BlacklistResponseBody.builder().error(e.getMessage()).build());
        }
    }

    @DeleteMapping(path = "/blacklist")
    public ResponseEntity<BlacklistResponseBody> deleteFromBlacklist(@RequestBody BlacklistRequestBody requestBody) {
        try {
            return ResponseEntity.ok(blacklistService.delete(requestBody.getPhoneNumbers()));
        } catch (Exception e) {
            LOGGER.error("Empty Input");
            return ResponseEntity.badRequest().body(BlacklistResponseBody.builder().error(e.getMessage()).build());
        } catch (InternalError e) {
            LOGGER.error("SQL Query Timed Out");
            return ResponseEntity.internalServerError().body(BlacklistResponseBody.builder().error(e.getMessage()).build());
        }
    }
}
