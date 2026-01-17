/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.infrastructure.iam.persistence.UserJpaRepository;
import com.trackee.infrastructure.iam.persistence.mapper.UserPersistenceMapper;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;

/**
 * @author vandunxg
 */
@Repository
@RequiredArgsConstructor
@Slf4j(topic = "USER-REPOSITORY")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepositoryImpl implements UserRepository {

    UserJpaRepository userJpaRepository;
    UserPersistenceMapper userPersistenceMapper;

    @Override
    public User save(User user) {
        log.info("[save] ={}", user.getId());

        userJpaRepository.save(userPersistenceMapper.toEntity(user));

        return user;
    }

    @Override
    public User findByEmail(String email) {

        return userJpaRepository
                .findByEmail(email)
                .map(userPersistenceMapper::toDomain)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }

    @Override
    public boolean existsByEmail(String email) {

        return userJpaRepository.existsByEmail(email);
    }
}
