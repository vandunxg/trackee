/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.persistence.entity;

import com.trackee.shared.infrastructure.persistence.AuditableEntity;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "otp_codes",
        indexes = {
            @Index(name = "otp_codes_hashed_code_idx", columnList = "hashed_code"),
            @Index(name = "otp_codes_deleted_at_idx", columnList = "deleted_at")
        })
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OtpCodeEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    UUID id;

    @Column(name = "user_id", nullable = false)
    UUID userId;

    @Column(name = "hashed_code", nullable = false)
    String hashedCode;

    @Column(name = "used_at")
    Instant usedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_purpose")
    OtpPurpose otpPurpose;

    @Column(name = "expiry_at", nullable = false)
    Instant expiryAt;

    @Column(name = "deleted_at")
    Instant deletedAt;
}
