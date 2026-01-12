/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import com.trackee.application.iam.port.AuthenticatePort;
import com.trackee.shared.exception.ResponseException;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.AuthenticationError;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATE-PORT")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticatePortImpl implements AuthenticatePort {

    AuthenticationManager authenticationManager;

    @Override
    public AuthenticatedUser authenticate(String email, String password) {
        log.info("[authenticate]={}", email);

        try {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new AuthenticatedUser(authentication.getName());
        } catch (AuthenticationException ex) {
            log.error("[authenticate]={}", ex.getMessage());

            throw new ResponseException(AuthenticationError.INVALID_CREDENTIALS);
        }
    }
}
