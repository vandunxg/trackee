/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam.request;

import com.trackee.shared.kernel.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Pattern(
                regexp = Constants.RegexPattern.PASSWORD_REGEX,
                message = "Password must contain at least one uppercase letter, one lowercase"
                        + " letter, one digit, and one special character")
        String password,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Pattern(
                regexp = Constants.RegexPattern.PASSWORD_REGEX,
                message = "Password must contain at least one uppercase letter, one lowercase"
                        + " letter, one digit, and one special character")
        String rePassword,

        @NotBlank(message = "Reset password token must not be blank")
        String resetPasswordToken) {
}
