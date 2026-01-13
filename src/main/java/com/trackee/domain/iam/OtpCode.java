/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import com.trackee.application.iam.command.OtpGenerateCmd;
import com.trackee.application.iam.port.OtpHasher;
import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

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

    public OtpCode(OtpGenerateCmd cmd, OtpHasher hasher) {

        this.id = UUID.randomUUID();
        this.userId = cmd.getUserId();
        this.otpPurpose = cmd.getOtpPurpose();
        this.expiresAt = cmd.getExpiresAt();

        this.hashedCode = hasher.hash(cmd.getCode());
    }
}
