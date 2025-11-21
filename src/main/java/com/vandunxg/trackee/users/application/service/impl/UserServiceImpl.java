package com.vandunxg.trackee.users.application.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.users.application.adapter.dto.UserInfoDTO;
import com.vandunxg.trackee.users.application.service.UserService;
import com.vandunxg.trackee.users.application.validator.UserValidator;
import com.vandunxg.trackee.users.domain.User;
import com.vandunxg.trackee.users.domain.UserRepository;
import com.vandunxg.trackee.users.domain.factory.UserFactory;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserFactory userFactory;

    UserRepository userRepository;

    UserValidator userValidator;

    VerificationTokenServiceImpl verificationTokenService;

    @Override
    @Transactional
    public UUID createUserRegistration(RegisterRequest request) {
        log.info("[createUserForRegistration] request={}", request);

        userValidator.validateEmailNotExists(request.email());

        User user = userFactory.createUserRegister(request);

        log.info("[register] save user to db");
        userRepository.save(user);

        verificationTokenService.generateRegistrationVerificationToken(
                user.getId(), request.email());

        return user.getId();
    }

    @Override
    public UserInfoDTO getUserInfo(UUID userId) {
        log.info("[getUserInfo] request={}", userId);

        User user = getUserById(userId);

        return new UserInfoDTO(
                user.getId(), user.getEmail(), user.getFullName(), user.getAvatarUrl());
    }

    @Override
    public void activeUser(UUID userId) {
        log.info("[activeUser] userId={}", userId);

        User user = getUserById(userId);
        user.ensureUserNotDeleted();

        user.activeUser();

        log.info("[activeUser] save user to db");
        userRepository.save(user);
    }

    User getUserById(UUID userId) {
        log.info("[getUserById] userID={}", userId);

        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
