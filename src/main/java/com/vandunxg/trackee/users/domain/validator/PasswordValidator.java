package com.vandunxg.trackee.users.domain.validator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;

@Slf4j(topic = "PASSWORD-VALIDATOR")
public final class PasswordValidator {

    private PasswordValidator() {}

    public static void validate(String password) {
        log.info("[validate] password={}", password);

        if (password == null || password.isBlank()) {
            throw new BusinessException(ErrorCode.PASSWORD_BLANK, HttpStatus.BAD_REQUEST);
        }

        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PASSWORD_TOO_SHORT, HttpStatus.BAD_REQUEST);
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_NO_UPPERCASE, HttpStatus.BAD_REQUEST);
        }

        if (!password.matches(".*[a-z].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_NO_LOWERCASE, HttpStatus.BAD_REQUEST);
        }

        if (!password.matches(".*\\d.*")) {
            throw new BusinessException(ErrorCode.PASSWORD_NO_DIGIT, HttpStatus.BAD_REQUEST);
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_NO_SPECIAL_CHAR, HttpStatus.BAD_REQUEST);
        }
    }
}
