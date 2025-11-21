package com.vandunxg.trackee.common.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vandunxg.trackee.common.api.response.ErrorResponse;
import com.vandunxg.trackee.common.error.ErrorCode;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, Locale locale) {

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        String message = messageSource.getMessage(errorCode.getMessageKey(), null, locale);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        log.debug("[Validation Error] {}", errors);
        return ResponseUtil.error(
                ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, message, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("[handleHttpMessageNotReadableException]={}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.REQUIRED_REQUEST_BODY;
        String message =
                messageSource.getMessage(errorCode.getMessageKey(), null, Locale.getDefault());

        return ResponseUtil.error(errorCode, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("[Unhandled Exception]", ex);

        String message =
                messageSource.getMessage(
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey(), null, Locale.getDefault());

        return ResponseUtil.error(
                ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
