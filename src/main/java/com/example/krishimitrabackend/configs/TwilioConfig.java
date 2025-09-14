package com.example.krishimitrabackend.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TwilioConfig {
    @Value("${app.twilio.account.sid}")
    private String ssid;

    @Value("${app.twilio.auth.token}")
    private String authToken;

    @Value("${app.twilio.my.phone.numer}")
    private String twilioNumber;
}
