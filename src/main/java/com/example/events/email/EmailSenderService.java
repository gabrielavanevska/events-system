package com.example.events.email;


public interface EmailSenderService {

    void sendEmail(String to, String subject, String message);

}
