/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.event;

import com.trackee.application.iam.port.OtpHasher;
import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.event.UserForgetPasswordEvent;
import com.trackee.domain.iam.event.UserRegisterEvent;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.infrastructure.common.security.OtpProperties;
import com.trackee.shared.kernel.application.mail.MailService;
import com.trackee.shared.kernel.domain.enums.MailPurpose;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.dto.MailMessage;
import com.trackee.shared.kernel.util.CodeGenerator;
import com.trackee.shared.kernel.util.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "OTP-EVENT-HANDLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpEventHandler {

  @NonFinal
  @Value("${application.base-url}")
  String baseUrl;

  OtpHasher otpHasher;
  MailService mailService;
  OtpCodeRepository otpCodeRepository;
  OtpProperties otpProperties;

  @Async("virtualThreadExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserRegisterEvent(UserRegisterEvent event) {
    log.info("[onUserRegisterEvent] userId={}", event.userId());

    String plainCode = generateOtp(Constants.CodeGenerator.OTP_CODE_LENGTH);
    Instant expiresAt = calculateExpiry(otpProperties.expiryTime());

    OtpCode otpCode =
            new OtpCode(otpHasher.hash(plainCode), OtpPurpose.REGISTER, event.userId(), expiresAt);

    otpCodeRepository.save(otpCode);

    Map<String, Object> variables =
            Map.of(
                    Constants.MailTemplateVars.FULL_NAME,
                    event.fullName(),
                    Constants.MailTemplateVars.OTP,
                    plainCode);

    MailMessage message = MailMessage.template(event.mailTo(), MailPurpose.REGISTER, variables);

    mailService.sendMail(message);
  }

  @Async("virtualThreadExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserForgetPassword(UserForgetPasswordEvent event) {
    log.info("[onUserForgetPassword] userId={}", event.userId());

    String plainCode = generateOtp(Constants.CodeGenerator.OTP_CODE_LENGTH);
    Instant expiresAt = calculateExpiry(otpProperties.expiryTime());

    OtpCode otpCode =
            new OtpCode(
                    otpHasher.hash(plainCode), OtpPurpose.FORGET_PASSWORD, event.userId(), expiresAt);

    otpCodeRepository.save(otpCode);

    String forgetPasswordUrl = String.format("%s/verify=%s", baseUrl, otpCode.getHashedCode());

    Map<String, Object> variables =
            Map.of(
                    Constants.MailTemplateVars.FULL_NAME,
                    event.fullName(),
                    Constants.MailTemplateVars.RESET_PASSWORD_LINK,
                    forgetPasswordUrl,
                    Constants.MailTemplateVars.EXPIRY_MINUTES,
                    otpProperties.expiryTime().toMinutes());

    MailMessage message =
            MailMessage.template(event.mailTo(), MailPurpose.FORGET_PASSWORD, variables);

    mailService.sendMail(message);
  }

  String generateOtp(int codeLength) {
    log.info("[generateOtp]");

    return CodeGenerator.numeric(codeLength);
  }

  Instant calculateExpiry(Duration ttl) {
    log.info("[calculateExpiry]");

    return Instant.now().plus(ttl);
  }
}
