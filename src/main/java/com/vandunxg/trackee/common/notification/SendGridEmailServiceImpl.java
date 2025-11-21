package com.vandunxg.trackee.common.notification;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
@Slf4j(topic = "SEND-EMAIL-SERVICE")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendGridEmailServiceImpl implements EmailService {

    @NonFinal
    @Value("${spring.application.url}")
    String BASE_URL;

    @NonFinal
    @Value("${spring.mail.from}")
    String MAIL_FROM;

    @NonFinal
    @Value("${spring.sendgrid.register-template}")
    String REGISTER_TEMPLATE_EMAIL;

    SendGrid sendGrid;

    @Override
    public void sendRegistrationConfirmationEmail(
            String toEmail, String fullName, String token, String otp) {

        log.info("[sendRegisterEmail] toEmail={}", toEmail);

        Map<String, Object> templateData =
                Map.of(
                        "full_name", fullName,
                        "otp", otp,
                        "verification_token", token);

        Mail mail = buildMail(toEmail, REGISTER_TEMPLATE_EMAIL, templateData);

        sendMailToSendGrid(mail, "register");
    }

    Mail buildMail(String toEmail, String templateId, Map<String, Object> dynamicData) {
        Email to = new Email(toEmail);
        Email from = new Email(MAIL_FROM);

        Personalization personalization = new Personalization();
        personalization.addTo(to);

        dynamicData.forEach(personalization::addDynamicTemplateData);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        mail.addPersonalization(personalization);

        return mail;
    }

    void sendMailToSendGrid(Mail mail, String mailType) {
        log.info("[sendMailToSendGrid] type={}", mailType);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            log.info(
                    "[sendMailToSendGrid] Sent {} mail successfully: status={}, body={}",
                    mailType,
                    response.getStatusCode(),
                    response.getBody());

        } catch (IOException e) {
            log.error(
                    "[sendMailToSendGrid] Failed to send {} mail: {}", mailType, e.getMessage(), e);
        }
    }
}
