package com.example.notificationservice.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sms_request_1")
public class SMS {

    @Id
    private String id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "message")
    private String Message;

    //  FAILED , IN_PROCESS or SUCCESS
    @Column(name = "status")
    private String Status;

    @Column(name = "failure_code")
    private int failureCode;

    @Column(name = "failure_comments")
    private String failureComments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
