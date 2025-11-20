package com.vandunxg.trackee.users.application.validator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.users.domain.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "USER-VALIDATOR")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidator {

    UserRepository userRepository;

    public void validateEmailNotExists(String email) {
        log.info("[validateEmailNotExists] email={}", email);

        if (userRepository.existsByEmail((email))) {
            throw new BusinessException(ErrorCode.USER_EMAIL_EXISTS);
        }
    }
}
