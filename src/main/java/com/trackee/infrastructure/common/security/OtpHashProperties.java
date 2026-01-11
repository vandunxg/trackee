/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.authentication.otp")
public record OtpHashProperties(@NotNull String secret) {}
