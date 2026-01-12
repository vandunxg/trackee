/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import com.trackee.infrastructure.iam.security.UserDetailServiceCustom;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author vandunxg
 */
@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "WEB-SECURITY-CONFIG")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfig {

    static String[] PUBLIC_ENDPOINT = {"/auth/**"};
    static String[] IGNORE_ENDPOINT = {
        "/actuator/**",
        "/v3/**",
        "/webjars/**",
        "/swagger-ui*/*swagger-initializer.js",
        "/swagger-ui*/**",
        "/favicon.ico"
    };

    UserDetailServiceCustom userDetailServiceCustom;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------------[FILTER - CHAIN]----------------");

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(PUBLIC_ENDPOINT)
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        });

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer ignoreResources() {

        return webSecurity -> webSecurity.ignoring().requestMatchers(IGNORE_ENDPOINT);
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
