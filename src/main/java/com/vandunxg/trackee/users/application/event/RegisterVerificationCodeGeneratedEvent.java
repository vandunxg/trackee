package com.vandunxg.trackee.users.application.event;

import java.util.UUID;

public record RegisterVerificationCodeGeneratedEvent(String otp, String token, UUID userId) {}
