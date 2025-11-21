package com.vandunxg.trackee.users.domain.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;

@Slf4j(topic = "EMAIL-VALIDATOR")
public final class EmailValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private EmailValidator() {}

    public static void validate(String email) {
        log.info("[validate] email={}", email);

        if (email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.EMAIL_BLANK, HttpStatus.BAD_REQUEST);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(ErrorCode.EMAIL_INVALID, HttpStatus.BAD_REQUEST);
        }
    }
}
