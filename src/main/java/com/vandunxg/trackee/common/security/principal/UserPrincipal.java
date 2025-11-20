package com.vandunxg.trackee.common.security.principal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vandunxg.trackee.common.enums.UserStatus;
import com.vandunxg.trackee.users.domain.User;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPrincipal implements UserDetails {

    @Getter User user;

    Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(user.getStatus());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserStatus.DELETED != user.getStatus();
    }
}
