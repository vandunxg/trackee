package com.vandunxg.trackee.common.notification;

public class SendGridEmailServiceImpl implements EmailService {

    @Override
    public void sendRegistrationConfirmationEmail(
            String toEmail, String fullName, String verificationLink, String verificationToken) {}
}
