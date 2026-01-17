/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;

/**
 * @author vandunxg
 */
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class OtpCode extends AuditableDomain {

    UUID id;
    UUID userId;
    String code;
    String hashedCode;
    Instant usedAt;
    Instant deletedAt;
    OtpPurpose otpPurpose;
    Instant expiresAt;

    public OtpCode(String hashedCode, OtpPurpose otpPurpose, UUID userId, Instant expiresAt) {

        this.id = UUID.randomUUID();
        this.userId = userId;
        this.otpPurpose = otpPurpose;
        this.expiresAt = expiresAt;

        this.hashedCode = hashedCode;
    }
}
