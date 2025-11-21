package com.vandunxg.trackee.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.common.security.principal.UserPrincipal;
import com.vandunxg.trackee.users.domain.User;
import com.vandunxg.trackee.users.domain.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CUSTOM-USER-DETAIL-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("[loadUserByUsername] email={}", email);

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new BusinessException(
                                                ErrorCode.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED));

        SimpleGrantedAuthority role =
                new SimpleGrantedAuthority(String.format("ROLE_%s", user.getRole()));

        return new UserPrincipal(user, List.of(role));
    }
}
