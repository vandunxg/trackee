/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.repository;

import com.trackee.domain.iam.User;

/**
 * @author vandunxg
 */
public interface UserRepository {

    User save(User user);

    User findByEmail(String email);
}
