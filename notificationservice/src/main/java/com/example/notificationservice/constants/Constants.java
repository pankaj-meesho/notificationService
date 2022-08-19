package com.example.notificationservice.constants;

public class Constants {
    public static class SQLTableNames {
        public static final String SMS_TABLE = "sms_request_1";
        public static final String BLACKLIST_TABLE = "blacklist_record";
    }

    public static class RedisConstants {
        public static final String REDIS_CACHE_NAME = "blacklist_numbers";
    }

    public static class ThirdPartyConstants{
        public static final String THIRD_PARTY_URL="https://api.imiconnect.in/resources/v1/messaging";
    }

    public static class RestTemplateConstants{
        public static final int READ_TIMEOUT = 4000;
        public static final int CONNECT_TIMEOUT = 4000;
    }
}
