/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "security.authentication.jwt")
public record JwtProperties(
        @NotBlank String keyStore,
        @NotBlank String keyStorePassword,
        @NotBlank String keyAlias,
        @NotNull Duration accessTokenExpiresIn,
        @NotNull Duration refreshTokenExpiresIn,
        @NotNull Duration refreshTokenLongExpiresIn,
        @NotNull Duration emailTokenExpiresIn) {}
