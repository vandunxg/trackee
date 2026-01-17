/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trackee.infrastructure.iam.persistence.entity.UserEntity;

/**
 * @author vandunxg
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
