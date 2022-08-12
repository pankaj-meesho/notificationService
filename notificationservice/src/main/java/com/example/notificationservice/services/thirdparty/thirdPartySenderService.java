package com.example.notificationservice.services.thirdparty;

import com.example.notificationservice.models.SMS;
import com.example.notificationservice.models.requestbody.ThirdPartyMessageRequestBody;
import com.example.notificationservice.models.responsebody.ThirdPartyMessageResponseBody;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class thirdPartySenderService {
    private static RestTemplate restTemplate;
    public static  String Send(SMS smsRecord) {
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


        HttpEntity<List<ThirdPartyMessageRequestBody>> request = new HttpEntity<>(Collections.singletonList(thirdPartyRequest));

        String thirdPartyURL = "https://api.imiconnect.in/resources/v1/messaging";
        String result = restTemplate
                .postForObject(thirdPartyURL, request, ThirdPartyMessageResponseBody.class)
                .getResponse().get(0).getDescription();
        return result;
    }
}
