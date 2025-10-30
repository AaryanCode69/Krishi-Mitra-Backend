package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.configs.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {
    private final TwilioConfig twilioConfig;

    public String sendSms(String phoneNumber,String otp){
        log.info("Sending SMS to phone number "+phoneNumber);
        Twilio.init(twilioConfig.getSsid(), twilioConfig.getAuthToken());

        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioConfig.getTwilioNumber()),
                    otp
            ).create();


            log.info("Twilio Message SID: {}, Status: {}", message.getSid(), message.getStatus());

            if (message.getStatus() == Message.Status.FAILED || message.getStatus() == Message.Status.UNDELIVERED) {

                String errorMessage = String.format(
                        "Twilio failed to send SMS to %s. SID: %s, Status: %s, Error Code: %s, Error Message: %s",
                        phoneNumber,
                        message.getSid(),
                        message.getStatus(),
                        message.getErrorCode(),
                        message.getErrorMessage()
                );
                log.warn(errorMessage);


                throw new RuntimeException("Failed to send SMS: " + message.getErrorMessage());
            }

            log.info("SMS request accepted by Twilio for phone number " + phoneNumber);
            return "Message sent Successfully";

        } catch (Exception e) {
            log.error("Exception in sendSms: {}", e.getMessage());
            throw e;
        }
    }
}
