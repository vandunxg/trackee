/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.repository;

import java.util.Optional;
import java.util.UUID;

import com.trackee.domain.iam.OtpCode;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;

/**
 * @author vandunxg
 */
public interface OtpCodeRepository {

    OtpCode save(OtpCode otpCode);

    Optional<OtpCode> findLatestByUserId(UUID userId, OtpPurpose otpPurpose);
}
