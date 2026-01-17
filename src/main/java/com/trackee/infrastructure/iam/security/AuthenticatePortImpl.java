/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.trackee.application.iam.port.AuthenticatePort;
import com.trackee.shared.exception.ResponseException;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.AuthenticationError;

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
            Authentication unauthenticated =
                    new UsernamePasswordAuthenticationToken(email, password);

            Authentication authenticated = authenticationManager.authenticate(unauthenticated);

            SecurityContextHolder.getContext().setAuthentication(authenticated);

            String role =
                    authenticated.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

            return new AuthenticatedUser(authenticated.getName(), role);
        } catch (AuthenticationException ex) {
            log.error("[authenticate] failed: {}", ex.getMessage());

            throw new ResponseException(AuthenticationError.INVALID_CREDENTIALS);
        }
    }
}
