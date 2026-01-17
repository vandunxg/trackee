/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.trackee.shared.kernel.util.Constants;

public record ActiveRequest(
        @NotBlank(message = "{EMAIL_REQUIRED}")
                @Email(message = "{EMAIL_INVALID_FORMAT}")
                @Size(max = 255, message = "{EMAIL_MAX_LENGTH}")
                String email,
        @NotBlank(message = "{CODE_REQUIRED}")
                @Size(min = 6, max = 8, message = "{CODE_LENGTH_INVALID}")
                @Pattern(
                        regexp = Constants.RegexPattern.OTP_CODE_REGEX,
                        message = "{CODE_INVALID_FORMAT}")
                String code) {}
