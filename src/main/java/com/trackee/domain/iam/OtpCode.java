/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import com.trackee.application.iam.port.OtpHasher;
import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.exception.BadRequestError;
import com.trackee.shared.kernel.exception.ResponseException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class OtpCode extends AuditableDomain {

    UUID id;
    UUID userId;
    String code;
    String hashedCode;
    Instant usedAt;
    Instant deletedAt;
    OtpPurpose otpPurpose;
    Instant expiresAt;
    Instant revokedAt;

    public OtpCode(String hashedCode, OtpPurpose otpPurpose, UUID userId, Instant expiresAt) {

        this.id = UUID.randomUUID();
        this.userId = userId;
        this.otpPurpose = otpPurpose;
        this.expiresAt = expiresAt;

        this.hashedCode = hashedCode;
    }

    public void verify(String rawCode, OtpHasher otpHasher) {

        if (isRevoked()) {
            throw new ResponseException(AuthenticationError.OTP_REVOKED);
        }

        if (isUsed()) {
            throw new ResponseException(BadRequestError.OTP_ALREADY_USED);
        }

        if (isExpired()) {
            throw new ResponseException(AuthenticationError.OTP_EXPIRED);
        }

        if (!otpHasher.matches(rawCode, this.hashedCode)) {
            throw new ResponseException(AuthenticationError.OTP_CODE_NOT_MATCH);
        }

        consume();
    }

    public void verifyForgetPassword() {

        if (isRevoked()) {
            throw new ResponseException(AuthenticationError.OTP_REVOKED);
        }

        if (isUsed()) {
            throw new ResponseException(BadRequestError.OTP_ALREADY_USED);
        }

        if (isExpired()) {
            throw new ResponseException(AuthenticationError.OTP_EXPIRED);
        }

        consume();
    }

    private boolean isRevoked() {

        return Objects.nonNull(this.revokedAt);
    }

    private boolean isExpired() {

        return this.expiresAt.isBefore(Instant.now());
    }

    private boolean isUsed() {

        return Objects.nonNull(usedAt);
    }

    private void consume() {
        this.usedAt = Instant.now();
    }

    public void revoked() {
        this.revokedAt = Instant.now();
    }
}
