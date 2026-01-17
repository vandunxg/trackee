/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

import com.trackee.shared.kernel.domain.enums.OtpPurpose;

/**
 * @author vandunxg
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpGenerateCmd {

    String code;
    UUID userId;
    OtpPurpose otpPurpose;
    Instant expiresAt;
}
