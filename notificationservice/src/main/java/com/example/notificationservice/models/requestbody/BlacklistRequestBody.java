package com.example.notificationservice.models.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BlacklistRequestBody {

    @JsonProperty("phone_numbers")
    private List<String> phoneNumbers;
}
