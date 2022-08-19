package com.example.notificationservice.services.blacklist;

import com.example.notificationservice.models.Blacklist.BlacklistRecord;
import com.example.notificationservice.models.responsebody.BlacklistResponseBody;
import com.example.notificationservice.repositories.BlacklistRepository;
import com.example.notificationservice.repositories.RedisCacheManager;
import com.example.notificationservice.utils.phoneNumberValidator;
import com.example.notificationservice.constants.Constants;
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
    public BlacklistResponseBody getBlacklist() {
//    return blacklistRepository.findAll();
        return BlacklistResponseBody.builder().phoneNumbers(redisCacheManager.findAll(Constants.RedisConstants.REDIS_CACHE_NAME)).build();
    }


    public BlacklistResponseBody addToBlacklist(List<String> phoneNumbers) throws Exception{
        if ((phoneNumbers != null) && (phoneNumbers.isEmpty())) {
            throw new Exception("List of phone numbers is mandatory");
        }

        List<String> invalidNumbers = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            if(phoneNumberValidator.isValidNumber(phoneNumber) == false)
            {
                invalidNumbers.add(phoneNumber);
                continue;
            }
                redisCacheManager.save(Constants.RedisConstants.REDIS_CACHE_NAME, phoneNumber);
            try {
                blacklistRepository.save(BlacklistRecord.builder().phoneNumber(phoneNumber).build());
            } catch (DataAccessException e) {
                throw new InternalError("Internal Server Error");
            }
        }
        return invalidNumbers.isEmpty() ?
                BlacklistResponseBody.builder().message(" Successfully blacklisted all").build()
                : BlacklistResponseBody.builder().phoneNumbers(invalidNumbers)
                .message(" Successfully blacklisted all except the following invalid numbers").build();
    }

    public BlacklistResponseBody delete(List<String> phoneNumbers) throws Exception {
        if (phoneNumbers != null && phoneNumbers.isEmpty()) {
            throw new Exception("List of phone numbers is mandatory");
        }
        List<String> failedNumbers = new ArrayList<>();

        for (String phoneNumber : phoneNumbers) {

            if (blacklistRepository.existsById(phoneNumber)) {
                redisCacheManager.delete(Constants.RedisConstants.REDIS_CACHE_NAME, phoneNumber);
                try {
                    blacklistRepository.deleteById(phoneNumber);
                } catch (DataAccessException e) {
                    throw new InternalError("Internal Server Error");
                }
            }
            else {
                failedNumbers.add(phoneNumber);
            }

        }

        return failedNumbers.isEmpty() ?
                BlacklistResponseBody.builder().message(" Successfully deleted all given numbers").build()
                : BlacklistResponseBody.builder().phoneNumbers(failedNumbers)
                .message(" Successfully deleted all except the following failed ones").build();

    }

}

