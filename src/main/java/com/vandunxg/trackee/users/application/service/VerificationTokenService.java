package com.vandunxg.trackee.users.application.service;

import java.util.UUID;

public interface VerificationTokenService {

    void generateRegistrationVerificationToken(UUID userId, String email);

    void verifyVerificationToken(String token, String userId);
}
