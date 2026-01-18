/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.email;

import com.trackee.infrastructure.common.security.SendGridProperties;
import com.trackee.shared.kernel.domain.enums.MailPurpose;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "SEND-GRID-TEMPLATE-RESOLVER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendGridTemplateResolver {

    SendGridProperties sendGridProperties;

    public String resolve(MailPurpose mailPurpose) {
        log.info("[resolve]={}", mailPurpose);

        return switch (mailPurpose) {
            case REGISTER -> sendGridProperties.template().register();
            case FORGET_PASSWORD -> sendGridProperties.template().forgetPassword();
        };
    }
}
