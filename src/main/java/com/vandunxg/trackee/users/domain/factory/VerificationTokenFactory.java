package com.vandunxg.trackee.users.domain.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vandunxg.trackee.users.domain.VerificationToken;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenFactory {

    @Value("${spring.application.verification.expiry-time}")
    static long VERIFICATION_EXPIRY_TIME;

    public VerificationToken createRegisterVerificationToken(
            UUID userId, String email, String otpHash, String tokenHash) {
        return VerificationToken.builder()
                .email(email)
                .userId(userId)
                .otpHash(otpHash)
                .tokenHash(tokenHash)
                .expiryAt(
                        new Date(System.currentTimeMillis() + VERIFICATION_EXPIRY_TIME).toInstant())
                .build();
    }
}
