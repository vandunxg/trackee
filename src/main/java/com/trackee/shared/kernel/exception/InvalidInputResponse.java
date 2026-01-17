/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author vandunxg
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvalidInputResponse extends ErrorResponse<Void> {

    private Set<FieldErrorResponse> errors;

    public InvalidInputResponse(
            int code, String message, String error, Set<FieldErrorResponse> errors) {
        super(code, message, (Void) null, error);
        this.errors = errors;
    }

    public InvalidInputResponse(int code, String message, String error) {
        super(code, message, (Void) null, error);
        this.errors = null;
    }

    public Set<FieldErrorResponse> getErrors() {
        return this.errors;
    }

    @Override
    public String toString() {
        return "InvalidInputResponse(errors=" + this.getErrors() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InvalidInputResponse)) {
            return false;
        } else {
            InvalidInputResponse other = (InvalidInputResponse) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$errors = this.getErrors();
                Object other$errors = other.getErrors();
                if (this$errors == null) {
                    if (other$errors != null) {
                        return false;
                    }
                } else if (!this$errors.equals(other$errors)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof InvalidInputResponse;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Object $errors = this.getErrors();
        result = result * 59 + ($errors == null ? 43 : $errors.hashCode());
        return result;
    }
}
