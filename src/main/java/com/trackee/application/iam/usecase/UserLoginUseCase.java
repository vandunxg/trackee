/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.trackee.application.iam.port.AuthenticatePort;
import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.infrastructure.common.security.TokenProvider;
import com.trackee.shared.exception.ResponseException;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.web.iam.request.LoginRequest;
import com.trackee.web.iam.response.LoginResponse;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-LOGIN-USE-CASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLoginUseCase {

    UserRepository userRepository;
    AuthenticatePort authenticatePort;
    TokenProvider tokenProvider;

    public LoginResponse login(LoginRequest request) {
        log.info("[register]={}", request);

        User user = userRepository.findByEmail(request.email());

        if (user == null) {
            throw new ResponseException(NotFoundError.USER_NOT_FOUND);
        }

        AuthenticatedUser authenticatedUser =
                authenticatePort.authenticate(request.email(), request.password());

        user.ensureUserActive();

        return new LoginResponse(
                tokenProvider.createToken(authenticatedUser, user.getId().toString()),
                tokenProvider.createRefreshToken(user.getId().toString()));
    }
}
