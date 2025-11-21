package com.vandunxg.trackee.users.domain.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.common.enums.RoleType;
import com.vandunxg.trackee.common.enums.UserStatus;
import com.vandunxg.trackee.users.domain.User;
import com.vandunxg.trackee.users.domain.validator.EmailValidator;
import com.vandunxg.trackee.users.domain.validator.PasswordValidator;

@Component
@Slf4j(topic = "USER-ADAPTER")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFactory {

    PasswordEncoder passwordEncoder;

    public User createUserRegister(RegisterRequest request) {
        log.info("[createUser] request={}", request);

        validateRequest(request);

        return User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .status(UserStatus.INACTIVE) // user is created but not activated yet.
                .role(RoleType.USER) // role always is USER
                .build();
    }

    void validateRequest(RegisterRequest request) {
        log.info("[validateRequest] request={}", request);

        EmailValidator.validate(request.email());
        PasswordValidator.validate(request.password());
    }
}
