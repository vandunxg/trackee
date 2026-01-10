/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.config;

import com.trackee.shared.kernel.util.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author vandunxg
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !Constants.System.ANONYMOUS_USER.equals(authentication.getPrincipal())) {

            return Optional.of(authentication.getName());
        }

        return Optional.of(Constants.System.SYSTEM);
    }
}
