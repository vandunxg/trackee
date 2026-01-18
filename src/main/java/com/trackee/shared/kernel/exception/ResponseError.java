/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

/**
 * @author vandunxg
 */
public interface ResponseError {
    String getName();

    String getMessage();

    int getStatus();

    default Integer getCode() {
        return 0;
    }
}
