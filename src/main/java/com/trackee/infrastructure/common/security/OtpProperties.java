/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.authentication.otp")
public record OtpProperties(@NotNull String secret, Duration expiryTime) {

    public OtpProperties {
        if (expiryTime == null) {
            expiryTime = Duration.ofMinutes(15);
        }
    }
}
