package com.example.notificationservice.models.responsebody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
@Data
public class BlacklistResponseBody {

    @Nullable
    @JsonProperty("phone_numbers")
    private List<String> phoneNumbers;

    @Nullable
    @JsonProperty("message")
    private String message;

    @Nullable
    @JsonProperty("error")
    private String error;

    public BlacklistResponseBody() {
    }

    public BlacklistResponseBody(List<String> phoneNumbers, String message,String error) {
        this.phoneNumbers = phoneNumbers;
        this.message = message;
        this.error  = error;
    }
}