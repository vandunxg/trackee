/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.User;
import com.trackee.domain.iam.event.UserResendOtpEvent;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.web.iam.request.ResendOtpRequest;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-RESEND-OTP-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResendOtpUseCase {

    OtpCodeRepository otpCodeRepository;
    UserRepository userRepository;
    ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Boolean resend(ResendOtpRequest request) {
        log.info("[resend]={}", request);

        User user = findUserByEmail(request.email());

        ensureUserNotActive(user);

        List<OtpCode> otpCodes = findAllOtpCodesNotUsedByUserId(user.getId(), OtpPurpose.REGISTER);

        otpCodes.forEach(OtpCode::revoked);

        applicationEventPublisher.publishEvent(
                new UserResendOtpEvent(user.getId(), user.getEmail(), user.getFullName()));

        otpCodeRepository.saveAll(otpCodes);

        return Boolean.TRUE;
    }

    List<OtpCode> findAllOtpCodesNotUsedByUserId(UUID userId, OtpPurpose otpPurpose) {
        log.info("[findAllOtpCodesNotUsedByUserId] userId={} purpose={}", userId, otpPurpose);

        return otpCodeRepository.findAllOtpCodesNotUsedByUserId(userId, otpPurpose);
    }

    void ensureUserNotActive(User user) {
        log.info("[ensureUserNotActive]={}", user.getId());

        if (user.isUserActive()) {
            throw new ResponseException(AuthenticationError.USER_ALREADY_ACTIVE);
        }
    }

    User findUserByEmail(String email) {
        log.info("[findUserByEmail]={}", email);

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }
}
