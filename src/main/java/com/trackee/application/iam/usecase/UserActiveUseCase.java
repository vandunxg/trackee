/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.application.iam.port.OtpHasher;
import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.application.port.TokenProvider;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.shared.kernel.util.Constants;
import com.trackee.web.iam.request.ActiveRequest;
import com.trackee.web.iam.response.LoginResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-ACTIVE-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserActiveUseCase {

    OtpHasher otpHasher;
    UserRepository userRepository;
    OtpCodeRepository otpCodeRepository;
    TokenProvider tokenProvider;

    @Transactional
    public LoginResponse active(ActiveRequest request) {
        log.info("[active]={}", request.email());

        User user = findUserByEmail(request.email());

        ensureUserNotActive(user);

        OtpCode otpCode = findLatestOtpByUserId(user.getId(), OtpPurpose.REGISTER);

        otpCode.verify(request.code(), otpHasher);

        user.activeUser();

        userRepository.save(user);
        otpCodeRepository.save(otpCode);

        List<String> authorities = List.of(Constants.ROLE_PREFIX + user.getRole().name());

        return new LoginResponse(
                tokenProvider.generateAccessToken(
                        new AuthenticatedUser(user.getEmail(), authorities), user.getId()),
                tokenProvider.generateRefreshToken(user.getId()));
    }

    void ensureUserNotActive(User user) {
        log.info("[ensureUserNotActive]={}", user.getId());

        if (user.isUserActive()) {
            throw new ResponseException(AuthenticationError.USER_ALREADY_ACTIVE);
        }
    }

    OtpCode findLatestOtpByUserId(UUID userId, OtpPurpose otpPurpose) {
        log.info("[findLatestOtpByUserId] userId={} purpose={}", userId, otpPurpose);

        return otpCodeRepository
                .findLatestByUserId(userId, otpPurpose)
                .orElseThrow(() -> new ResponseException(NotFoundError.OTP_CODE_NOT_FOUND));
    }

    User findUserByEmail(String email) {
        log.info("[findUserByEmail]={}", email);

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }
}
