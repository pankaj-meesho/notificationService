package com.example.notificationservice.models.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SmsRequestBody {
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("message")
    private String message;

}
