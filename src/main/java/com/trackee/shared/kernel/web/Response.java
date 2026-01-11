/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trackee.shared.kernel.exception.BadRequestError;
import com.trackee.shared.kernel.exception.ErrorCodeClient;
import com.trackee.shared.kernel.exception.ResponseError;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author vandunxg
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {
    protected T data;
    private boolean success = true;
    private int code = 200;
    private String message;
    @Setter private long timestamp = Instant.now().toEpochMilli();
    private String status;
    @JsonIgnore private RuntimeException exception;

    public Response() {
        this.status = ErrorCodeClient.SUCCESS.name();
    }

    public static <T> Response<T> of(T res) {
        Response<T> response = new Response<T>();
        response.data = res;
        response.success();
        return response;
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<T>();
        response.success();
        return response;
    }

    public static <T> Response<T> fail(RuntimeException exception) {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(ErrorCodeClient.FAIL.name());
        response.setException(exception);
        return response;
    }

    public static <T> Response<T> fail(String message) {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(ErrorCodeClient.FAIL.name());
        response.setMessage(message);
        response.setCode(BadRequestError.BAD_REQUEST_ERROR.getStatus());
        return response;
    }

    public static <T> Response<T> fail(String message, RuntimeException exception) {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(ErrorCodeClient.FAIL.name());
        response.setException(exception);
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> fail() {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(ErrorCodeClient.FAIL.name());
        return response;
    }

    public Response<T> success() {
        this.success = true;
        this.code = 200;
        this.status = ErrorCodeClient.SUCCESS.name();
        return this;
    }

    public Response<T> data(T res) {
        this.data = res;
        return this;
    }

    public Response<T> success(String message) {
        this.success = true;
        this.message = message;
        this.code = 200;
        this.status = ErrorCodeClient.SUCCESS.name();
        return this;
    }

    public Response<T> fail(String message, ResponseError responseError) {
        this.success = false;
        this.code = responseError.getCode();
        this.status = ErrorCodeClient.FAIL.name();
        if (StringUtils.hasText(message)) {
            this.message = message;
        } else {
            this.message = responseError.getMessage();
        }

        return this;
    }

    public Response<T> fail(Exception ex, ResponseError responseError) {
        this.success = false;
        this.code = responseError.getCode();
        this.status = ErrorCodeClient.FAIL.name();
        this.message = ex.getMessage();
        return this;
    }

    @Override
    public String toString() {
        return "Response {data="
                + this.data
                + ", success="
                + this.success
                + ", status="
                + this.status
                + ", code="
                + this.code
                + ", message='"
                + this.message
                + "', timestamp="
                + this.timestamp
                + ", exception="
                + this.exception
                + "}";
    }

    @JsonIgnore
    public void setException(final RuntimeException exception) {
        this.exception = exception;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Response)) {
            return false;
        } else {
            Response<?> other = (Response) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else if (this.getTimestamp() != other.getTimestamp()) {
                return false;
            } else {
                Object thisData = this.getData();
                Object otherData = other.getData();
                if (thisData == null) {
                    if (otherData != null) {
                        return false;
                    }
                } else if (!thisData.equals(otherData)) {
                    return false;
                }

                Object thisMessage = this.getMessage();
                Object otherMessage = other.getMessage();
                if (thisMessage == null) {
                    if (otherMessage != null) {
                        return false;
                    }
                } else if (!thisMessage.equals(otherMessage)) {
                    return false;
                }

                Object thisStatus = this.getStatus();
                Object otherStatus = other.getStatus();
                if (thisStatus == null) {
                    if (otherStatus != null) {
                        return false;
                    }
                } else if (!thisStatus.equals(otherStatus)) {
                    return false;
                }

                Object thisException = this.getException();
                Object otherException = other.getException();
                if (thisException == null) {
                    return otherException == null;
                } else {
                    return thisException.equals(otherException);
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Response;
    }

    @Override
    public int hashCode() {
        int prime = 59;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        result = result * 59 + this.getCode();
        long timestamp = this.getTimestamp();
        result = result * 59 + Long.hashCode(timestamp);
        Object data = this.getData();
        result = result * 59 + (data == null ? 43 : data.hashCode());
        Object message = this.getMessage();
        result = result * 59 + (message == null ? 43 : message.hashCode());
        Object status = this.getStatus();
        result = result * 59 + (status == null ? 43 : status.hashCode());
        Object exception = this.getException();
        result = result * 59 + (exception == null ? 43 : exception.hashCode());
        return result;
    }
}
