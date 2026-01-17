/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.trackee.shared.infrastructure.i18n.LocaleStringService;
import com.trackee.shared.kernel.exception.*;

/**
 * @author vandunxg
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j(topic = "EXCEPTION-HANDLE-ADVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExceptionHandleAdvice {

    LocaleStringService localeStringService;

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<ErrorResponse<Void>> handleObjectOptimisticLockingFailureException(
            ObjectOptimisticLockingFailureException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.LOCKED.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.RECORD_IS_BEING_UPDATED.getName(),
                                        "Transaction is being locked",
                                        new Object[0]),
                                BadRequestError.RECORD_IS_BEING_UPDATED.getName()));
    }

    private void catchException(Exception exception) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            requestAttributes.setAttribute("custom_exception_message", exception, 0);
        }
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message(e.getMessage())
                        .build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.INVALID_INPUT.getName(),
                                errors));
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<ErrorResponse<Void>> handleIllegalStateException(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request file Upload" + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet();
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.FILE_SIZE_EXCEEDED.getName(),
                                        BadRequestError.FILE_SIZE_EXCEEDED.getMessage(),
                                        new Object[0]),
                                BadRequestError.INVALID_INPUT.getName(),
                                errors));
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMissingPathVariableException(
            MissingPathVariableException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message("Missing path variable")
                        .build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                BadRequestError.MISSING_PATH_VARIABLE.getCode(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.MISSING_PATH_VARIABLE.getName(),
                                errors));
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMissingRequestHeaderException(
            MissingRequestHeaderException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message("Missing request header")
                        .build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                BadRequestError.MISSING_PATH_VARIABLE.getCode(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.MISSING_PATH_VARIABLE.getName(),
                                errors));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse<Void>> handleRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getMethod())
                        .message("Http request method not support")
                        .build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.METHOD_NOT_ALLOWED.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.INVALID_INPUT.getName(),
                                errors));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            ConstraintViolationException e, HttpServletRequest request) {
        Set<FieldErrorResponse> errors = new HashSet();

        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            String queryParamPath = constraintViolation.getPropertyPath().toString();
            log.debug("queryParamPath = {}", queryParamPath);
            String queryParam =
                    queryParamPath.contains(".")
                            ? queryParamPath.substring(queryParamPath.indexOf(".") + 1)
                            : queryParamPath;
            String object =
                    queryParamPath.split("\\.").length > 1
                            ? queryParamPath.substring(
                                    queryParamPath.indexOf(".") + 1,
                                    queryParamPath.lastIndexOf("."))
                            : queryParamPath;
            String errorMessage =
                    this.localeStringService.getMessage(
                            constraintViolation.getMessage(),
                            constraintViolation.getMessage(),
                            new Object[0]);
            errors.add(
                    FieldErrorResponse.builder()
                            .field(queryParam)
                            .objectName(object)
                            .message(errorMessage)
                            .build());
        }

        InvalidInputResponse invalidInputResponse;
        if (!CollectionUtils.isEmpty(errors)) {
            long count = errors.size();
            invalidInputResponse =
                    errors.stream()
                            .skip(count - 1L)
                            .findFirst()
                            .map(
                                    (fieldErrorResponse) ->
                                            new InvalidInputResponse(
                                                    HttpStatus.BAD_REQUEST.value(),
                                                    this.localeStringService.getMessage(
                                                            BadRequestError.INVALID_INPUT.getName(),
                                                            "Invalid request arguments",
                                                            new Object[0]),
                                                    fieldErrorResponse.getObjectName(),
                                                    errors))
                            .orElse(null);
        } else {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.getName(),
                            errors);
        }

        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        String message = "Invalid input format";
        Throwable cause = e.getCause();
        InvalidInputResponse invalidInputResponse;
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldPath =
                    (String)
                            invalidFormatException.getPath().stream()
                                    .map(JsonMappingException.Reference::getFieldName)
                                    .collect(Collectors.joining("."));
            if (invalidFormatException.getTargetType() != null
                    && invalidFormatException.getTargetType().isEnum()) {
                message =
                        String.format(
                                "Invalid enum value: '%s' for the field: '%s'. The value must be"
                                        + " one of: %s.",
                                invalidFormatException.getValue(),
                                fieldPath,
                                Arrays.toString(
                                        invalidFormatException.getTargetType().getEnumConstants()));
            }

            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.name(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(fieldPath)
                                            .message(message)
                                            .build()));
        } else if (cause instanceof JsonParseException jsonParseException) {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.name(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    jsonParseException
                                                            .getProcessor()
                                                            .getCurrentName())
                                            .message("Invalid input format")
                                            .build()));
        } else if (cause instanceof MismatchedInputException mismatchedInputException) {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    (String)
                                                            mismatchedInputException
                                                                    .getPath()
                                                                    .stream()
                                                                    .map(
                                                                            JsonMappingException
                                                                                            .Reference
                                                                                    ::getFieldName)
                                                                    .collect(
                                                                            Collectors.joining(
                                                                                    ".")))
                                            .message("Mismatched input")
                                            .build()));
        } else if (cause instanceof JsonMappingException jsonMappingException) {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    (String)
                                                            jsonMappingException.getPath().stream()
                                                                    .map(
                                                                            JsonMappingException
                                                                                            .Reference
                                                                                    ::getFieldName)
                                                                    .collect(
                                                                            Collectors.joining(
                                                                                    ".")))
                                            .message("Json mapping invalid")
                                            .build()));
        } else {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            this.localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    "Invalid request arguments",
                                    new Object[0]),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder().message("Invalid input").build()));
        }

        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
    }

    @ExceptionHandler({com.trackee.shared.exception.ResponseException.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(
            com.trackee.shared.exception.ResponseException e, HttpServletRequest request) {
        log.warn(
                "Failed to handle request {}: {}",
                new Object[] {request.getRequestURI(), e.getError().getMessage(), e});
        ResponseError error = e.getError();
        String message =
                this.localeStringService.getMessage(
                        error.getName(), e.getError().getMessage(), e.getParams());
        this.catchException(e);
        return ResponseEntity.status(error.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(error.getCode())
                                .error(error.getName())
                                .message(message)
                                .build());
    }

    @ExceptionHandler({InvocationTargetException.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(
            InvocationTargetException e, HttpServletRequest request) {
        log.warn(
                "Failed to handle request {}: {}",
                new Object[] {request.getRequestURI(), e.getMessage(), e});
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error(
                "Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(),
                e);
        String msg =
                this.localeStringService.getMessage(
                        InternalServerError.INTERNAL_SERVER_ERROR.getName(),
                        "There are somethings wrong: {0}",
                        new Object[] {e});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(error.getCode())
                                .error(error.getName())
                                .message(msg)
                                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        Set fieldErrors =
                bindingResult.getAllErrors().stream()
                        .map(
                                (objectError) -> {
                                    try {
                                        FieldError fieldError = (FieldError) objectError;
                                        String message =
                                                this.localeStringService.getMessage(
                                                        fieldError.getDefaultMessage(),
                                                        fieldError.getDefaultMessage(),
                                                        new Object[0]);
                                        return FieldErrorResponse.builder()
                                                .field(fieldError.getField())
                                                .objectName(fieldError.getObjectName())
                                                .message(message)
                                                .build();
                                    } catch (ClassCastException var4) {
                                        return null;
                                    }
                                })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
        Logger var10000 = log;
        String var10001 = request.getRequestURI();
        var10000.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.INVALID_INPUT.getName(),
                                fieldErrors));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(
            Exception e, HttpServletRequest request) {
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error(
                "Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(),
                e);
        String msg =
                this.localeStringService.getMessage(
                        InternalServerError.INTERNAL_SERVER_ERROR.getName(),
                        "There are somethings wrong: {0}",
                        new Object[] {e});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(error.getCode())
                                .error(error.getName())
                                .message(msg)
                                .build());
    }

    @ExceptionHandler({
        DataIntegrityViolationException.class,
        NonTransientDataAccessException.class,
        DataAccessException.class
    })
    public ResponseEntity<ErrorResponse<Object>> handleDataAccessException(
            DataAccessException e, HttpServletRequest request) {
        ResponseError error = InternalServerError.DATA_ACCESS_EXCEPTION;
        log.error(
                "Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(),
                e);
        log.error(e.getMessage(), e);
        String msg =
                this.localeStringService.getMessage(
                        InternalServerError.DATA_ACCESS_EXCEPTION.getName(),
                        "Data access exception",
                        new Object[] {e.getClass().getName()});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(error.getCode())
                                .error(error.getName())
                                .message(msg)
                                .build());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        String message =
                this.localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(),
                        "Invalid request arguments",
                        new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                message,
                                BadRequestError.INVALID_INPUT.getName(),
                                Collections.singleton(
                                        FieldErrorResponse.builder()
                                                .field(e.getParameterName())
                                                .message(e.getMessage())
                                                .build())));
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MissingServletRequestPartException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        String message =
                this.localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(),
                        "Invalid request arguments",
                        new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                message,
                                BadRequestError.INVALID_INPUT.getName()));
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MultipartException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        String message =
                this.localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(),
                        "Invalid request arguments",
                        new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                message,
                                BadRequestError.INVALID_INPUT.getName()));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            BindException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        Set fieldsErrors =
                e.getFieldErrors().stream()
                        .map(
                                (fieldError) ->
                                        FieldErrorResponse.builder()
                                                .field(fieldError.getField())
                                                .objectName(fieldError.getObjectName())
                                                .build())
                        .collect(Collectors.toSet());
        String message =
                this.localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(),
                        "Invalid request arguments",
                        new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                message,
                                BadRequestError.INVALID_INPUT.name(),
                                fieldsErrors));
    }

    @ExceptionHandler({MismatchedInputException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MismatchedInputException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new InvalidInputResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                this.localeStringService.getMessage(
                                        BadRequestError.INVALID_INPUT.getName(),
                                        "Invalid request arguments",
                                        new Object[0]),
                                BadRequestError.INVALID_INPUT.getName(),
                                Collections.singleton(
                                        FieldErrorResponse.builder()
                                                .message(e.getMessage())
                                                .build())));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse<Object>> handleValidationException(
            AccessDeniedException e, HttpServletRequest request) {
        Logger var10000 = log;
        String var10001 = request.getMethod();
        var10000.warn(
                "Failed to handle request "
                        + var10001
                        + ": "
                        + request.getRequestURI()
                        + ": "
                        + e.getMessage(),
                e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponse.builder()
                                .code(AuthorizationError.ACCESS_DENIED.getCode())
                                .error(AuthorizationError.ACCESS_DENIED.getName())
                                .message(
                                        this.localeStringService.getMessage(
                                                AuthorizationError.ACCESS_DENIED.getName(),
                                                "Access Denied",
                                                new Object[0]))
                                .build());
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<ErrorResponse<Void>> handleValidationException(
            InsufficientAuthenticationException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        ResponseEntity.BodyBuilder var10000 = ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        ErrorResponse.ErrorResponseBuilder var10001 =
                ErrorResponse.builder().error(AuthenticationError.UNAUTHORISED.getName());
        String var10002 = request.getMethod();
        return var10000.body(
                var10001.message(
                                "You were not authorized to request "
                                        + var10002
                                        + " "
                                        + request.getRequestURI())
                        .build());
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponse<Object>> handleValidationException(
            InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .error(AuthenticationError.UNAUTHORISED.getName())
                                .message(e.getMessage())
                                .build());
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse<Object>> handleNotFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .error(NotFoundError.NOT_FOUND.getName())
                                .message(e.getMessage())
                                .build());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse<Object>> handleValidationException(
            BadCredentialsException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .error(AuthenticationError.UNAUTHORISED.getName())
                                .message(e.getMessage())
                                .build());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse<Object>> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .error(AuthenticationError.UNAUTHORISED.getName())
                                .code(AuthenticationError.UNAUTHORISED.getCode())
                                .message(
                                        this.localeStringService.getMessage(
                                                AuthorizationError.NOT_SUPPORTED_AUTHENTICATION
                                                        .getName(),
                                                "Your authentication has not been supported yet",
                                                new Object[0]))
                                .build());
    }
}
