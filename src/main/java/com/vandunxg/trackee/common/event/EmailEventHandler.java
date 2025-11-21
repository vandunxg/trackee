package com.vandunxg.trackee.common.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.vandunxg.trackee.common.notification.EmailService;
import com.vandunxg.trackee.users.application.adapter.UserAdapter;
import com.vandunxg.trackee.users.application.adapter.dto.UserInfoDTO;
import com.vandunxg.trackee.users.application.event.RegisterVerificationCodeGeneratedEvent;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "USER-EVENT-HANDLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailEventHandler {

    @NonFinal
    @Value("${spring.application.url}")
    String BASE_URL;

    EmailService emailService;
    UserAdapter userAdapter;

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRegisterVerificationCodeGenerated(RegisterVerificationCodeGeneratedEvent event) {
        log.info("[onRegisterVerificationCodeGenerated]: {}", event);

        UserInfoDTO userInfo = userAdapter.getUserInfo(event.userId());

        emailService.sendRegistrationConfirmationEmail(
                userInfo.email(),
                userInfo.fullName(),
                buildVerificationLink(event.token(), userInfo.userId()),
                event.otp());
    }

    String buildVerificationLink(String token, UUID userId) {
        log.info("[buildVerificationLink]: {}", token);

        return String.format("%s/auth/verify/token=%s?userId=%s", BASE_URL, token, userId);
    }
}
