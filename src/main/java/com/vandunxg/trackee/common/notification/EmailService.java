package com.vandunxg.trackee.common.notification;

public interface EmailService {

    void sendRegistrationConfirmationEmail(
            String toEmail, String fullName, String verificationLink, String verificationToken);
}
