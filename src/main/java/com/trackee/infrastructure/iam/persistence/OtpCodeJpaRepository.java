/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence;

import com.trackee.infrastructure.iam.persistence.entity.OtpCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vandunxg
 */
public interface OtpCodeJpaRepository extends JpaRepository<OtpCodeEntity, UUID> {}
