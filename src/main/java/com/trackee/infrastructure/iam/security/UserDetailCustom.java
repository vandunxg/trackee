/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trackee.domain.iam.User;
import com.trackee.shared.kernel.util.Constants;

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
        return List.of(new SimpleGrantedAuthority(Constants.ROLE_PREFIX + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.isUserActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
}
