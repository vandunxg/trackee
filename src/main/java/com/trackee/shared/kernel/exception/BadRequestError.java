/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

import lombok.Getter;

/**
 * @author vandunxg
 */
@Getter
public enum BadRequestError implements ResponseError {
    INVALID_INPUT(40000001, "Invalid input : {0}"),
    INVALID_ACCEPT_LANGUAGE(40000002, "Invalid value for request header Accept-Language: {0}"),
    MISSING_PATH_VARIABLE(40000003, "Missing path variable"),
    PATH_INVALID(40000004, "Path is invalid"),
    UNDEFINED(40000005, ""),
    FILE_SIZE_EXCEEDED(40000006, "File size exceeds the limit"),
    RECORD_IS_BEING_UPDATED(4000007, "The record is being updated. Please wait a minute"),
    MISSING_HEADER_VARIABLE(4000008, "Missing header variable: {0}"),
    BAD_REQUEST_ERROR(4000009, "Bad request error"),
    EMAIL_ALREADY_EXISTS(4000010, "Email already exists"),
    OTP_ALREADY_USED(4000011, "OTP already used"),
    RE_PASSWORD_NOT_MATCH(4000012, "RePassword not match");

    private final Integer code;
    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
