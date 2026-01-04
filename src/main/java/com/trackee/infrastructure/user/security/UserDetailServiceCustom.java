/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.security;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public record UserDetailServiceCustom(UserRepository userRepository) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        return new UserDetailCustom(user);
    }
}
