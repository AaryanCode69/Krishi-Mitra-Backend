package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.configs.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {
    private TwilioConfig twilioConfig;

    public String sendSms(String phoneNumber,String otp){
        log.info("Sending SMS to phone number "+phoneNumber);
        Twilio.init(twilioConfig.getSsid(), twilioConfig.getAuthToken());
        Message.creator(new PhoneNumber(phoneNumber),new PhoneNumber(twilioConfig.getTwilioNumber()),otp).create();
        log.info("SMS sent to phone number "+phoneNumber);
        return "Message sent Successfully";
    }
}
