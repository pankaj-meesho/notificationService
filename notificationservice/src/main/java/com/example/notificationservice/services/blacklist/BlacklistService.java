package com.example.notificationservice.services.blacklist;

import com.example.notificationservice.models.Blacklist.BlacklistRecord;
import com.example.notificationservice.models.responsebody.BlacklistResponseBody;
import com.example.notificationservice.repositories.BlacklistRepository;
import com.example.notificationservice.repositories.RedisCacheManager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final RedisCacheManager redisCacheManager;
    private  final String REDIS_CACHE_NAME = "blacklist_numbers";

    public BlacklistResponseBody getBlacklist() {
        return BlacklistResponseBody.builder().phoneNumbers(redisCacheManager.findAll(REDIS_CACHE_NAME)).build();
    }


    public BlacklistResponseBody addToBlacklist(List<String> phoneNumbers) throws Exception{
        if ((phoneNumbers != null) && (phoneNumbers.isEmpty())) {
            throw new Exception("List of phone numbers is mandatory");
        }

        for (String phoneNumber : phoneNumbers) {
            redisCacheManager.save(REDIS_CACHE_NAME, phoneNumber);
            blacklistRepository.save(BlacklistRecord.builder().phoneNumber(phoneNumber).build());
        }
        return BlacklistResponseBody.builder().message(" Successfully blacklisted all given numbers").build();
    }

    public BlacklistResponseBody delete(List<String> phoneNumbers) throws Exception {
        if (phoneNumbers != null && phoneNumbers.isEmpty()) {
            throw new Exception("List of phone numbers is mandatory");
        }

        for (String phoneNumber : phoneNumbers) {
            if (blacklistRepository.existsById(phoneNumber)) {
                redisCacheManager.delete(REDIS_CACHE_NAME, phoneNumber);
                blacklistRepository.deleteById(phoneNumber);
            }

        }
        return BlacklistResponseBody.builder().message(" Successfully deleted all given numbers").build();

    }

}
