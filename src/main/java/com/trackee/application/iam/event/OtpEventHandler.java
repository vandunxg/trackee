/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.trackee.application.iam.port.OtpHasher;
import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.event.UserResendOtpEvent;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.infrastructure.common.security.OtpProperties;
import com.trackee.shared.kernel.application.mail.MailService;
import com.trackee.shared.kernel.domain.enums.MailPurpose;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.dto.MailMessage;
import com.trackee.shared.kernel.util.CodeGenerator;
import com.trackee.shared.kernel.util.Constants;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "OTP-EVENT-HANDLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpEventHandler {

    OtpHasher otpHasher;
    MailService mailService;
    OtpCodeRepository otpCodeRepository;
    OtpProperties otpProperties;

    @Async("virtualThreadExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserResendOtpEvent(UserResendOtpEvent event) {
        log.info("[onUserResendOtpEvent]={}", event);

        String code = CodeGenerator.numeric(Constants.CodeGenerator.OTP_CODE_LENGTH);

        Duration ttl = otpProperties.expiryTime();
        Instant expiresAt = Instant.now().plus(ttl);

        OtpCode otpCode =
                new OtpCode(otpHasher.hash(code), OtpPurpose.REGISTER, event.userId(), expiresAt);

        Map<String, Object> variables = new HashMap<>();

        variables.put("full_name", event.fullName());
        variables.put("otp", code);

        MailMessage message = MailMessage.template(event.mailTo(), MailPurpose.REGISTER, variables);

        otpCodeRepository.save(otpCode);

        mailService.sendMail(message);
    }
}
