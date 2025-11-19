package com.vandunxg.trackee.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // AUTH
    AUTH_001("AUTH_001", "auth.invalid.credentials");

    // USER

    private final String code;
    private final String messageKey;

    ErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
