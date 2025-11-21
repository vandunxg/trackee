package com.vandunxg.trackee.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;

import com.vandunxg.trackee.common.error.ErrorCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessException extends RuntimeException {

    ErrorCode errorCode;
    HttpStatus status;

    public BusinessException(ErrorCode errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, HttpStatus.BAD_REQUEST);
    }
}
