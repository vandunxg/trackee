/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgetPasswordRequest(
        @NotBlank(message = "{EMAIL_REQUIRED}")
        @Email(message = "{EMAIL_INVALID_FORMAT}")
        @Size(max = 255, message = "{EMAIL_MAX_LENGTH}")
        String email) {
}
