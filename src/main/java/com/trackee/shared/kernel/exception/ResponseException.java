/* Copyright (c) 2026 Trackee */
package com.trackee.shared.exception;

import com.trackee.shared.kernel.exception.ResponseError;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author vandunxg
 */
public class ResponseException extends RuntimeException {

    private final ResponseError error;
    private final Object[] params;

    public ResponseException(ResponseError error) {
        this(error.getMessage(), (Throwable) null, (ResponseError) error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, (Object) null);
    }

    public ResponseException(
            String message, Throwable cause, ResponseError error, Object... params) {
        super(
                Objects.nonNull(message)
                        ? MessageFormat.format(message, params)
                        : error.getMessage(),
                cause);
        this.error = error;
        this.params = params == null ? new Object[0] : params;
    }

    public ResponseException(ResponseError error, Object... params) {
        this(error.getMessage(), (Throwable) null, error, params);
    }

    public ResponseException(String message, ResponseError error) {
        this(message, (Throwable) null, (ResponseError) error);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, (Throwable) null, error, params);
    }

    public ResponseError getError() {
        return this.error;
    }

    public Object[] getParams() {
        return this.params;
    }
}
