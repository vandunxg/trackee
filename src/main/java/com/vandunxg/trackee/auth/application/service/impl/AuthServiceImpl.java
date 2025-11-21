package com.vandunxg.trackee.auth.application.service.impl;

import io.jsonwebtoken.lang.Strings;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vandunxg.trackee.auth.api.dto.LoginRequest;
import com.vandunxg.trackee.auth.api.dto.LoginResponse;
import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.auth.api.dto.RegisterResponse;
import com.vandunxg.trackee.auth.application.service.AuthService;
import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.common.security.jwt.JwtTokenProvider;
import com.vandunxg.trackee.common.security.principal.UserPrincipal;
import com.vandunxg.trackee.users.application.adapter.UserAdapter;
import com.vandunxg.trackee.users.application.adapter.VerificationCodeAdapter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    static int COOKIES_MAX_AGE = 7 * 24 * 60 * 60;
    static String REFRESH_TOKEN_PATH = "/auth/refresh";

    UserAdapter userAdapter;
    VerificationCodeAdapter verificationCodeAdapter;
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtProvider;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("[register] request={}", request);

        UUID userId = userAdapter.createdUser(request);

        return new RegisterResponse(userId);
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        log.info("[login] request={}", request);

        UserPrincipal userPrincipal = authenticate(request);

        String refreshToken = jwtProvider.refreshToken(userPrincipal);
        String accessToken = jwtProvider.accessToken(userPrincipal);

        addRefreshTokenToCookie(response, refreshToken);

        return new LoginResponse(accessToken);
    }

    @Override
    public void verifyVerificationToken(String token, String userId) {
        log.info("[verifyVerificationToken] token={}, userId={}", token, userId);

        if (!Strings.hasLength(token) || token.isBlank()) {
            log.error("[verifyVerificationToken] token is empty or blank, token={}", token);

            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_FORMAT_INVALID);
        }

        if (!isValidUUID(userId)) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_FORMAT_INVALID);
        }

        log.info(
                "[verifyVerificationToken] verify verification token, token={}, userId={}",
                token,
                userId);
        verificationCodeAdapter.verifyVerificationToken(token, userId);

        log.info("[verifyVerificationToken] active user={} after verify successfully", userId);
        userAdapter.activeUser(UUID.fromString(userId));

        log.info(
                "[verifyVerificationToken] verify successfully, token={}, userId={}",
                token,
                userId);
    }

    UserPrincipal authenticate(LoginRequest request) {
        log.info("[authenticate] request={}", request);

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(), request.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return (UserPrincipal) authentication.getPrincipal();
        } catch (DisabledException e) {
            log.warn("[authenticate] user inactive email={}", request.email());

            throw new BusinessException(ErrorCode.USER_INACTIVE, HttpStatus.FORBIDDEN);

        } catch (AccountExpiredException e) {
            log.info("[authenticate] user account deleted={}", request.email());

            throw new BusinessException(ErrorCode.USER_DELETED, HttpStatus.FORBIDDEN);
        } catch (BadCredentialsException e) {
            log.warn("[authenticate] bad credentials email={}", request.email());

            throw new BusinessException(
                    ErrorCode.AUTH_INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);

        } catch (AuthenticationException e) {
            log.error("[authenticate] auth error email={} msg={}", request.email(), e.getMessage());

            throw new BusinessException(
                    ErrorCode.AUTH_INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
    }

    void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        log.info("[addRefreshTokenToCookie] refreshToken={}", refreshToken.substring(0, 10));

        Cookie cookie = new Cookie("refresh_token", refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(REFRESH_TOKEN_PATH);
        cookie.setMaxAge(COOKIES_MAX_AGE);
        cookie.setAttribute("SameSite", "Strict");

        response.addCookie(cookie);
    }

    boolean isValidUUID(String value) {
        log.info("[isValidUUID] value={}", value);

        try {
            UUID.fromString(value);
            return true;
        } catch (Exception ex) {
            log.error("[isValidUUID] value={} error={}", value, ex.getMessage());
            return false;
        }
    }
}
