/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trackee.shared.kernel.web.Response;

/**
 * @author vandunxg
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends Response<T> {
    private String error;

    public ErrorResponse(int code, String message, T data, String error) {
        this.setData(data);
        this.setCode(code);
        this.setMessage(message);
        this.setSuccess(false);
        this.setStatus(ErrorCodeClient.FAIL.name());
        this.error = error;
    }

    public static <T> ErrorResponseBuilder<T> builder() {
        return new ErrorResponseBuilder<T>();
    }

    @Override
    public String toString() {
        return "ErrorResponse(error=" + this.getError() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorResponse)) {
            return false;
        } else {
            ErrorResponse<?> other = (ErrorResponse) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$error = this.getError();
                Object other$error = other.getError();
                if (this$error == null) {
                    if (other$error != null) {
                        return false;
                    }
                } else if (!this$error.equals(other$error)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ErrorResponse;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Object $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        return result;
    }

    public static class ErrorResponseBuilder<T> {
        private int code;
        private String message;
        private T data;
        private String error;

        ErrorResponseBuilder() {}

        public ErrorResponseBuilder<T> code(final int code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder<T> message(final String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public ErrorResponseBuilder<T> error(final String error) {
            this.error = error;
            return this;
        }

        public ErrorResponse<T> build() {
            return new ErrorResponse<T>(this.code, this.message, this.data, this.error);
        }

        @Override
        public String toString() {
            return "ErrorResponse.ErrorResponseBuilder(code="
                    + this.code
                    + ", message="
                    + this.message
                    + ", data="
                    + this.data
                    + ", error="
                    + this.error
                    + ")";
        }
    }
}
