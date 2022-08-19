package com.example.notificationservice.services.thirdparty;

import com.example.notificationservice.models.SMS;
import com.example.notificationservice.models.requestbody.ThirdPartyMessageRequestBody;
import com.example.notificationservice.models.responsebody.ThirdPartyMessageResponseBody;

import com.example.notificationservice.services.kafka.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdPartySenderService {
    public final RestTemplate restTemplate;
    private static final Logger LOGGER = LogManager.getLogger(ThirdPartySenderService.class);

    public String send(SMS smsRecord) throws ResourceAccessException{
        ThirdPartyMessageRequestBody thirdPartyRequest = ThirdPartyMessageRequestBody.builder().channels(ThirdPartyMessageRequestBody
                        .Channels.builder()
                        .sms(ThirdPartyMessageRequestBody.Channels
                                .Sms.builder()
                                .text(smsRecord.getMessage()).build()).build())
                .deliveryChannel("sms")
                .destination(Collections.singletonList(ThirdPartyMessageRequestBody
                        .Destination.builder()
                        .msisdn(Collections.singletonList(smsRecord.getPhoneNumber()))
                        .correlationId(smsRecord.getId()).build())).build();

        HttpHeaders headers = new HttpHeaders();
        String headerKey = "key";
        String headerValue = "93ceffda-5941-11ea-9da9-025282c394f2";
        headers.set(headerKey, headerValue);
        HttpEntity<List<ThirdPartyMessageRequestBody>> request = new HttpEntity<>(Collections.singletonList(thirdPartyRequest),headers);

        String thirdPartyURL = "https://api.imiconnect.in/resources/v1/messaging";
        String result = restTemplate.postForObject(thirdPartyURL, request, ThirdPartyMessageResponseBody.class)
                .getResponse().get(0).getDescription();


        LOGGER.info("Third party API result => {}", result);
        return result;
    }
}
