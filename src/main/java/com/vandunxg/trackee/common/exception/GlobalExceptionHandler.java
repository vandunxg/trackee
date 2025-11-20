package com.vandunxg.trackee.common.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vandunxg.trackee.common.api.response.ErrorResponse;
import com.vandunxg.trackee.common.util.ResponseUtil;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j(topic = "GLOBAL-EXCEPTION-HANDLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, Locale locale) {
        log.info("[handleBusinessException] ex={}", ex.getMessage());

        String message = messageSource.getMessage(ex.getErrorCode().getMessageKey(), null, locale);

        return ResponseUtil.error(ex.getErrorCode(), ex.getStatus(), message);
    }
}
