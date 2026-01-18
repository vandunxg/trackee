/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "send-grid")
public record SendGridProperties(String apiKey, Template template, Mail mail) {

    public record Mail(String from) {
    }

    public record Template(String register, String forgetPassword) {
    }
}
