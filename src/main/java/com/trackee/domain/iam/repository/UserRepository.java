/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.repository;

import com.trackee.domain.iam.User;

import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
public interface UserRepository {

    Optional<User> findById(UUID id);

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
