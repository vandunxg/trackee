/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trackee.infrastructure.iam.persistence.entity.OtpCodeEntity;

/**
 * @author vandunxg
 */
public interface OtpCodeJpaRepository extends JpaRepository<OtpCodeEntity, UUID> {}
