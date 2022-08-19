package com.example.notificationservice.models.responsebody;

import com.example.notificationservice.models.SMS;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
public class SmsResponseBody {
    @JsonProperty("data")
    private List<SMS> data;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("error")
    private String error;


    public SmsResponseBody(List<SMS> data, String comments,String error) {
        this.data = data;
        this.comments = comments;
        this.error=error;
    }
}
