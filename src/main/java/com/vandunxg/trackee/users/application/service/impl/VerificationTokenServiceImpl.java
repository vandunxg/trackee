package com.vandunxg.trackee.users.application.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.common.util.TokenHasher;
import com.vandunxg.trackee.common.util.VerificationCodeGenerator;
import com.vandunxg.trackee.users.application.event.RegisterVerificationCodeGeneratedEvent;
import com.vandunxg.trackee.users.application.service.VerificationTokenService;
import com.vandunxg.trackee.users.domain.VerificationToken;
import com.vandunxg.trackee.users.domain.VerificationTokenRepository;
import com.vandunxg.trackee.users.domain.factory.VerificationTokenFactory;
import com.vandunxg.trackee.users.domain.validator.VerificationTokenValidator;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "VERIFICATION-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenServiceImpl implements VerificationTokenService {

    static int OTP_LENGTH = 6;
    static int VERIFICATION_TOKEN_BYTE = 16;

    VerificationTokenFactory verificationTokenFactory;
    VerificationCodeGenerator verificationCodeGenerator;
    VerificationTokenRepository verificationTokenRepository;
    ApplicationEventPublisher eventPublisher;
    VerificationTokenValidator verificationTokenValidator;
    TokenHasher tokenHasher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRegistrationVerificationToken(UUID userId, String email) {
        log.info("[generateRegistrationVerificationToken] userId={}, email={}", userId, email);

        String otpCode = verificationCodeGenerator.generateOtp(OTP_LENGTH);
        String token = verificationCodeGenerator.generateVerificationToken(VERIFICATION_TOKEN_BYTE);

        String otpCodeHash = tokenHasher.hashToken(otpCode);
        String tokenHash = tokenHasher.hashToken(token);

        VerificationToken verificationToken =
                verificationTokenFactory.createRegisterVerificationToken(
                        userId, email, otpCodeHash, tokenHash);

        log.info("[generateRegistrationVerificationToken] save verification code to db");
        verificationTokenRepository.save(verificationToken);

        log.info(
                "[generateRegistrationVerificationToken] publish an event after generated verification code");
        eventPublisher.publishEvent(
                new RegisterVerificationCodeGeneratedEvent(otpCode, token, userId));
    }

    @Override
    public void verifyVerificationToken(String token, String userId) {
        log.info("[verifyVerificationCode] token={}, userId={}", token, userId);

        String tokenHash = tokenHasher.hashToken(token);
        VerificationToken verificationToken =
                getVerificationTokenByUserIdAndToken(UUID.fromString(userId), tokenHash);

        verificationTokenValidator.validateToken(verificationToken, UUID.fromString(userId));

        verificationToken.markAsUsed();

        log.info("[verifyVerificationCode] save verification token to db");
        verificationTokenRepository.save(verificationToken);
    }

    VerificationToken getVerificationTokenByUserIdAndToken(UUID userId, String tokenHash) {
        log.info("[getVerificationTokenByUserIdAndToken] userId={} token={}", userId, tokenHash);

        return verificationTokenRepository
                .findByTokenHashAndUserId(tokenHash, userId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_NOT_FOUND));
    }
}
