/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence.impl;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.infrastructure.iam.persistence.UserJpaRepository;
import com.trackee.infrastructure.iam.persistence.mapper.UserPersistenceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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
    public Optional<User> findById(UUID id) {
        log.info("[findById]={}", id);

        return userJpaRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public User save(User user) {
        log.info("[save] ={}", user.getId());

        userJpaRepository.save(userPersistenceMapper.toEntity(user));

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("[findByEmail]={}", email);

        return userJpaRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("[existsByEmail]={}", email);

        return userJpaRepository.existsByEmail(email);
    }
}
