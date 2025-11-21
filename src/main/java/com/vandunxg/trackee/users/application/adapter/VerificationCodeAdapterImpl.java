package com.vandunxg.trackee.users.application.adapter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.vandunxg.trackee.users.application.service.VerificationTokenService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "VERIFICATION-CODE-ADAPTER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationCodeAdapterImpl implements VerificationCodeAdapter {

    VerificationTokenService verificationTokenService;

    @Override
    public void verifyVerificationToken(String token, String userId) {
        log.info("[verifyVerificationToken] token={}, userId={}", token, userId);

        verificationTokenService.verifyVerificationToken(token, userId);
    }
}
