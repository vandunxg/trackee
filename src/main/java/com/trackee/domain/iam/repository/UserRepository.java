/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.repository;

import java.util.Optional;

import com.trackee.domain.iam.User;

/**
 * @author vandunxg
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
