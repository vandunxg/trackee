/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;

@Service
public record UserDetailServiceCustom(UserRepository userRepository) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository
                .findByEmail(username)
                .map(UserDetailCustom::new)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
    }
}
