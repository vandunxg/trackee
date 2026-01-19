/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.application.port.TokenProvider;
import com.trackee.shared.kernel.exception.BadRequestError;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.web.iam.request.ResetPasswordRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-VERIFY-FORGET-PASSWORD-OTP-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResetPasswordUseCase {

    TokenProvider tokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public Boolean resetPassword(ResetPasswordRequest request) {
        log.info("[resetPassword]");

        validateRequest(request);

        String password = request.password();

        String userId = tokenProvider.validateEmailToken(request.resetPasswordToken());

        User user = findUserById(UUID.fromString(userId));

        user.changePassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return Boolean.TRUE;
    }

    void validateRequest(ResetPasswordRequest request) {
        log.info("[validateRequest]");

        validatePasswordRequest(request);
    }

    void validatePasswordRequest(ResetPasswordRequest request) {
        log.info("[validatePasswordRequest]");

        if (!Objects.equals(request.password(), request.rePassword())) {
            throw new ResponseException(BadRequestError.RE_PASSWORD_NOT_MATCH);
        }
    }

    User findUserById(UUID userId) {
        log.info("[findUserById]={}", userId);

        return userRepository.findById(userId).orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }
}
