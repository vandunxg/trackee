/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import com.trackee.domain.iam.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author vandunxg
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailCustom implements UserDetails {

    User user;

    public UserDetailCustom(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }
}
