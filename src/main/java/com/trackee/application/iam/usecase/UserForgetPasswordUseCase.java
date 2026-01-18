/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.event.UserForgetPasswordEvent;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.web.iam.request.ForgetPasswordRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-FORGET-PASSWORD-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserForgetPasswordUseCase {

    UserRepository userRepository;
    ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Boolean forgetPassword(ForgetPasswordRequest request) {
        log.info("[forgetPassword]={}", request);

        User user = findUserByEmail(request.email());

        applicationEventPublisher.publishEvent(
                new UserForgetPasswordEvent(user.getId(), user.getFullName(), user.getEmail()));

        return Boolean.TRUE;
    }

    User findUserByEmail(String email) {
        log.info("[findUserByEmail] email={}", email);

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }
}
