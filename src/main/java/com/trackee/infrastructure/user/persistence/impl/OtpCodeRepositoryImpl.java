/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.persistence.impl;

import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.infrastructure.user.persistence.OtpCodeJpaRepository;
import com.trackee.infrastructure.user.persistence.mapper.OtpCodePersistenceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
