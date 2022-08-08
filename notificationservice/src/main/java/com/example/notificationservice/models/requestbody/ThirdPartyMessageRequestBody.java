package com.example.notificationservice.models.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ThirdPartyMessageRequestBody {
    @JsonProperty("deliverychannel")
    private String deliveryChannel;

    @JsonProperty("channels")
    private Channels channels;

    @JsonProperty("destination")
    private List<Destination> destination;

    @Data
    @Builder
    public static class Destination {
        @JsonProperty("msisdn")
        private List<String> msisdn;

        @JsonProperty("correlationid")
        private String correlationId;
    }

    @Data
    @Builder
    public static class Channels {
        @JsonProperty("sms")
        private Sms sms;

        @Data
        @Builder
        public static class Sms {
            @JsonProperty("text")
            private String text;
        }
    }

    public ThirdPartyMessageRequestBody(String deliveryChannel, Channels channels, List<Destination> destination) {
        this.deliveryChannel = deliveryChannel;
        this.channels = channels;
        this.destination = destination;
    }

//    public ThirdPartyMessageRequestBody() {
//    }

}
