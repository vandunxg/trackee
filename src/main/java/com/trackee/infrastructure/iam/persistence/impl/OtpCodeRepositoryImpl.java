/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.infrastructure.iam.persistence.OtpCodeJpaRepository;
import com.trackee.infrastructure.iam.persistence.mapper.OtpCodePersistenceMapper;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;

/**
 * @author vandunxg
 */
@Repository
@RequiredArgsConstructor
@Slf4j(topic = "OTP-CODE-REPOSITORY")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpCodeRepositoryImpl implements OtpCodeRepository {

    OtpCodePersistenceMapper otpCodePersistenceMapper;
    OtpCodeJpaRepository otpCodeJpaRepository;

    @Override
    public OtpCode save(OtpCode otpCode) {
        log.info("[save]={}", otpCode);

        otpCodeJpaRepository.save(otpCodePersistenceMapper.toEntity(otpCode));

        return otpCode;
    }

    @Override
    public Optional<OtpCode> findLatestByUserId(UUID userId, OtpPurpose otpPurpose) {
        log.info("[findLatestByUserId] userId={} purpose={}", userId, otpPurpose);

        return otpCodeJpaRepository
                .findLatestOtpCodeByUserId(userId, otpPurpose)
                .map(otpCodePersistenceMapper::toDomain);
    }
}
