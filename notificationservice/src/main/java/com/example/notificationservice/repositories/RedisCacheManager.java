package com.example.notificationservice.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class RedisCacheManager {
    @Qualifier("common")
    @Autowired
    RedisTemplate template;

    public String save(String key, String value) {
        template.opsForSet().add(key, value);
        return value;
    }

    public List<String> findAll(String key) {
        Set<String> members = template.opsForSet().members(key);
        return new ArrayList<>(members);
    }

    public String delete(String key, String value) {
        template.opsForSet().remove(key, value);
        return value;
    }

    public boolean checkById(String key, String value) {
        return template.opsForSet().isMember(key, value);
    }

    public boolean isBlacklisted(String phoneNumber) {
        return checkById("blacklist_numbers",phoneNumber);
    }
}
