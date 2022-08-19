package com.example.notificationservice.services.pagination;

import com.example.notificationservice.models.requestbody.SmsRequestBody;
import com.example.notificationservice.models.responsebody.SmsResponseBody;
import com.example.notificationservice.repositories.SmsRepository;
import com.example.notificationservice.models.SMS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.data.domain.Page;

import  java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class paginationService {

    private final SmsRepository smsRepository;
    private static int pageSize = 2;

    public Page<SMS> getPage(int pageNumber) {
        Pageable PageWithTwoElements = PageRequest.of(pageNumber, pageSize);
        Page<SMS> currentListOfSms = smsRepository.findAll(PageWithTwoElements);
        return currentListOfSms;
    }
    public SmsResponseBody searchText(String text) {
        int currentPageNumber = 0;
        ArrayList<SMS> validSms = new ArrayList<>();

        while(true) {
            Page<SMS> currentListOfSms = getPage(currentPageNumber);
            if(currentListOfSms.isEmpty())
                break;;

                for(SMS Sms : currentListOfSms) {
                    String message = Sms.getMessage();
                    if(message.contains(text))
                        validSms.add(Sms);
                }

            currentPageNumber++;
        }

        SmsResponseBody smsResponsebody = SmsResponseBody.builder().data(validSms).comments("Successfully returned all messages").build();
        return smsResponsebody;
    }
    public SmsResponseBody getSmsBetweenDateAndTime(SmsRequestBody smsRequestBody) throws Exception {
        int currentPageNumber = 0;
        ArrayList<SMS> validSms = new ArrayList<>();

        LocalDateTime startTime = smsRequestBody.getStartTime();
        LocalDateTime endTime = smsRequestBody.getEndTime();

        if (startTime.isAfter(endTime)) {
            throw new Exception("Start Time must be lesser than End Time");
        }

        while(true) {
            Page<SMS> currentListOfSms = getPage(currentPageNumber);
            if(currentListOfSms.isEmpty())
                break;;

            for(SMS Sms : currentListOfSms) {
                LocalDateTime smsCreationTime = Sms.getCreatedAt();

                if(smsCreationTime.isAfter(endTime) || smsCreationTime.isBefore(startTime))
                    continue;

                validSms.add(Sms);
            }

            currentPageNumber++;
        }

        SmsResponseBody smsResponsebody = SmsResponseBody.builder().data(validSms).comments("Successfully returned all messages").build();
        return smsResponsebody;
    }
}
