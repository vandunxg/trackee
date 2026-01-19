/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import com.trackee.application.iam.port.AuthenticatePort;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.exception.ResponseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

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
            Authentication unauthenticated = new UsernamePasswordAuthenticationToken(email, password);

            Authentication authenticated = authenticationManager.authenticate(unauthenticated);

            SecurityContextHolder.getContext().setAuthentication(authenticated);

            List<String> authorities = authenticated.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new AuthenticatedUser(authenticated.getName(), authorities);
        } catch (DisabledException ex) {
            log.warn("[authenticate] user disabled: {}", email);
            throw new ResponseException(AuthenticationError.USER_NOT_ACTIVE);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            log.warn("[authenticate] bad credentials: {}", ex.getMessage());

            throw new ResponseException(AuthenticationError.INVALID_CREDENTIALS);

        } catch (AuthenticationException ex) {
            log.error("[authenticate] unexpected auth error", ex);
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        }
    }
}
