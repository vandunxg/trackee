/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.persistence;

import com.trackee.infrastructure.user.persistence.entity.OtpCodeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vandunxg
 */
public interface OtpCodeJpaRepository extends JpaRepository<OtpCodeEntity, UUID> {}
