/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author vandunxg
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldErrorResponse implements Serializable {

    String field;
    String objectName;
    String message;

    FieldErrorResponse(final String field, final String objectName, final String message) {
        this.field = field;
        this.objectName = objectName;
        this.message = message;
    }

    public static FieldErrorResponseBuilder builder() {
        return new FieldErrorResponseBuilder();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FieldErrorResponse other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object thisField = this.getField();
                Object otherField = other.getField();
                if (thisField == null) {
                    if (otherField != null) {
                        return false;
                    }
                } else if (!thisField.equals(otherField)) {
                    return false;
                }

                Object thisObjectName = this.getObjectName();
                Object otherObjectName = other.getObjectName();
                if (thisObjectName == null) {
                    if (otherObjectName != null) {
                        return false;
                    }
                } else if (!thisObjectName.equals(otherObjectName)) {
                    return false;
                }

                Object thisMessage = this.getMessage();
                Object otherMessage = other.getMessage();
                if (thisMessage == null) {
                    return otherMessage == null;
                } else {
                    return thisMessage.equals(otherMessage);
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FieldErrorResponse;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object field = this.getField();
        result = result * 59 + (field == null ? 43 : field.hashCode());
        Object objectName = this.getObjectName();
        result = result * 59 + (objectName == null ? 43 : objectName.hashCode());
        Object message = this.getMessage();
        result = result * 59 + (message == null ? 43 : message.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String var10000 = this.getField();
        return "FieldErrorResponse(field="
                + var10000
                + ", objectName="
                + this.getObjectName()
                + ", message="
                + this.getMessage()
                + ")";
    }

    public static class FieldErrorResponseBuilder {
        private String field;
        private String objectName;
        private String message;

        FieldErrorResponseBuilder() {}

        public FieldErrorResponseBuilder field(final String field) {
            this.field = field;
            return this;
        }

        public FieldErrorResponseBuilder objectName(final String objectName) {
            this.objectName = objectName;
            return this;
        }

        public FieldErrorResponseBuilder message(final String message) {
            this.message = message;
            return this;
        }

        public FieldErrorResponse build() {
            return new FieldErrorResponse(this.field, this.objectName, this.message);
        }

        @Override
        public String toString() {
            return "FieldErrorResponse.FieldErrorResponseBuilder(field="
                    + this.field
                    + ", objectName="
                    + this.objectName
                    + ", message="
                    + this.message
                    + ")";
        }
    }
}
