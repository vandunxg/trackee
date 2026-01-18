/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.email;

import com.sendgrid.SendGrid;
import com.trackee.infrastructure.common.security.SendGridProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vandunxg
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendGridConfig {

    SendGridProperties sendGridProperties;

    @Bean
    public SendGrid sendGrid() {

        return new SendGrid(sendGridProperties.apiKey());
    }
}
