package com.example.notificationservice.repositories;

import com.example.notificationservice.models.SMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SmsRepository extends JpaRepository<SMS, String>{
    // all crud operations
}
