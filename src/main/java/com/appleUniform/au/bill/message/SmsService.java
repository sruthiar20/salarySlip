package com.appleUniform.au.bill.message;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private static final String ACCOUNT_SID = "AC520945406ec280fa67b4e324cdf384c8";
    private static final String AUTH_TOKEN = "86eac884cfa9d7eaadda0cacc1dc5c7e";
    private static final String TWILIO_PHONE_NUMBER = "+12676992952";

    public SmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSMS(String to, String messageBody) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody
        ).create();

        System.out.println("SMS Sent: " + message.getSid());
    }
}