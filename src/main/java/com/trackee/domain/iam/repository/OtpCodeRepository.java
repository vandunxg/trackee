/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.repository;

import com.trackee.domain.iam.OtpCode;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
public interface OtpCodeRepository {

    OtpCode save(OtpCode otpCode);

    Optional<OtpCode> findLatestByUserId(UUID userId, OtpPurpose otpPurpose);

    List<OtpCode> findAllOtpCodesNotUsedByUserId(UUID userId, OtpPurpose otpPurpose);

    List<OtpCode> saveAll(List<OtpCode> otpCodes);

    Optional<OtpCode> findByHashedCode(String hashedCode);
}
