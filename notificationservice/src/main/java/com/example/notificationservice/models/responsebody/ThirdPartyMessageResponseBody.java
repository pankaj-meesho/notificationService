package com.example.notificationservice.models.responsebody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ThirdPartyMessageResponseBody {
    @JsonProperty("response")
    private List<Response> response;

    @Data
    public static class Response {
        @JsonProperty("code")
        private int code;

        @JsonProperty("transid")
        private String transId;

        @JsonProperty("description")
        private String description;

        @JsonProperty("correlationid")
        private String correlationId;
    }
}
