package com.example.notificationservice.repositories;

import com.example.notificationservice.models.Blacklist.BlacklistRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistRecord, String> {

}