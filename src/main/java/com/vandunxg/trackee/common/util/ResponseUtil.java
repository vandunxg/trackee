package com.vandunxg.trackee.common.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vandunxg.trackee.common.api.response.ApiResponse;
import com.vandunxg.trackee.common.api.response.ErrorResponse;
import com.vandunxg.trackee.common.error.ErrorCode;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResponseUtil {

    static MessageSource messageSource;

    ResponseUtil() {}

    static String getPath() {
        var attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr != null ? attr.getRequest().getRequestURI() : "unknown";
    }

    // ---------------------------------------
    // SUCCESS RESPONSE
    // ---------------------------------------

    public static <T> ResponseEntity<ApiResponse<T>> success(
            String message, T data, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(
                        ApiResponse.<T>builder()
                                .message(message)
                                .data(data)
                                .path(getPath())
                                .timestamp(Instant.now())
                                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return success(message, data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(null, data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success("Created successfully", data, HttpStatus.CREATED);
    }

    public static ResponseEntity<ApiResponse<Void>> deleted() {
        return success("Deleted successfully", null, HttpStatus.NO_CONTENT);
    }

    // ---------------------------------------
    // ERROR RESPONSE
    // ---------------------------------------

    public static ResponseEntity<ErrorResponse> error(
            ErrorCode code, HttpStatus status, String message, Map<String, String> errors) {

        ErrorResponse body =
                ErrorResponse.builder()
                        .code(code.getCode())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .path(getPath())
                        .errors(errors)
                        .timestamp(Instant.now())
                        .build();

        return ResponseEntity.status(status).body(body);
    }

    public static ResponseEntity<ErrorResponse> error(
            ErrorCode code, HttpStatus status, String message) {
        return error(code, status, message, null);
    }

    public static ResponseEntity<ErrorResponse> error(ErrorCode code, HttpStatus status) {
        return error(code, status, null, null);
    }
}
