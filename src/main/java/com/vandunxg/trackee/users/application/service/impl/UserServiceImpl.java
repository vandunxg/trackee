package com.vandunxg.trackee.users.application.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
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

    @Override
    public UUID createUserRegistration(RegisterRequest request) {
        log.info("[createUserForRegistration] request={}", request);

        userValidator.validateEmailNotExists(request.email());

        User user = userFactory.createUserRegister(request);

        log.info("[register] save user to db");
        userRepository.save(user);

        return user.getId();
    }
}
