/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.trackee.infrastructure.common.security.SendGridProperties;
import com.trackee.shared.kernel.application.mail.MailService;
import com.trackee.shared.kernel.domain.enums.MailPurpose;
import com.trackee.shared.kernel.dto.MailMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "MAIL-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendGridMailServiceImpl implements MailService {

    SendGrid sendGrid;
    SendGridProperties sendGridProperties;
    SendGridTemplateResolver templateResolver;

    @Override
    public void sendMail(MailMessage message) {
        log.info("[sendMail]={}", message);

        String templateId = templateResolver.resolve(message.mailPurpose());

        message
                .to()
                .forEach(
                        to -> {
                            Mail mail = buildMail(to, templateId, message.variables());
                            sendMailToSendGrid(mail, message.mailPurpose());
                        });
    }

    Mail buildMail(String toEmail, String templateId, Map<String, Object> dynamicData) {
        log.info("[buildMail] toEmail={} templdateId={} data={}", toEmail, templateId, dynamicData);

        Email to = new Email(toEmail);
        Email from = new Email(sendGridProperties.mail().from());

        Personalization personalization = new Personalization();
        personalization.addTo(to);

        dynamicData.forEach(personalization::addDynamicTemplateData);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        mail.addPersonalization(personalization);

        return mail;
    }

    void sendMailToSendGrid(Mail mail, MailPurpose mailPurpose) {
        log.info("[sendMailToSendGrid] type={}", mailPurpose);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            log.info(
                    "[sendMailToSendGrid] Sent {} mail successfully: status={}, body={}",
                    mailPurpose,
                    response.getStatusCode(),
                    response.getBody());

        } catch (IOException e) {
            log.error("[sendMailToSendGrid] Failed to send {} mail: {}", mailPurpose, e.getMessage(), e);
        }
    }
}
