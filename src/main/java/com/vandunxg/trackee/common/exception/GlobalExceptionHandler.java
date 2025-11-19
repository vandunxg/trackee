package com.vandunxg.trackee.common.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessException ex, Locale locale) {

        String message = messageSource.getMessage(ex.getErrorCode().getMessageKey(), null, locale);

        ErrorResponse error = new ErrorResponse(ex.getErrorCode().getCode(), message);

        return ResponseEntity.badRequest().body(error);
    }
}
