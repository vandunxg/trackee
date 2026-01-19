/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author vandunxg
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvalidInputResponse extends ErrorResponse<Void> {

    private Set<FieldErrorResponse> errors;

    public InvalidInputResponse(int code, String message, String error, Set<FieldErrorResponse> errors) {
        super(code, message, null, error);
        this.errors = errors;
    }

    public InvalidInputResponse(int code, String message, String error) {
        super(code, message, null, error);
        this.errors = null;
    }

    @Override
    public String toString() {
        return "InvalidInputResponse(errors=" + this.getErrors() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InvalidInputResponse other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object thisErrors = this.getErrors();
                Object otherErrors = other.getErrors();
                if (thisErrors == null) {
                    return otherErrors == null;
                } else {
                    return thisErrors.equals(otherErrors);
                }
            }
        }
    }

    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof InvalidInputResponse;
    }

    @Override
    public int hashCode() {
        int prime = 59;
        int result = super.hashCode();
        Object errors = this.getErrors();
        result = result * prime + (errors == null ? 43 : errors.hashCode());
        return result;
    }
}
