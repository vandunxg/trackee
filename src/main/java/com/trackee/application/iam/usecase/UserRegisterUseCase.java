/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.event.UserRegisterEvent;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.exception.BadRequestError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.web.iam.request.RegisterRequest;
import com.trackee.web.iam.response.UserRegisterResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-REGISTER-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisterUseCase {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserRegisterResponse register(RegisterRequest request) {
        log.info("[register]={}", request);

        ensureEmailNotExists(request.email());

        User user = User.register(request.fullName(), request.email(), passwordEncoder.encode(request.password()));

        eventPublisher.publishEvent(new UserRegisterEvent(user.getId(), user.getEmail(), user.getFullName()));

        userRepository.save(user);

        return new UserRegisterResponse(user.getId());
    }

    void ensureEmailNotExists(String email) {
        log.info("[ensureEmailNotExists]={}", email);

        if (userRepository.existsByEmail(email)) {
            throw new ResponseException(BadRequestError.EMAIL_ALREADY_EXISTS);
        }
    }
}
