package com.vandunxg.trackee.auth.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vandunxg.trackee.auth.api.dto.LoginRequest;
import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.auth.application.service.AuthService;
import com.vandunxg.trackee.common.constant.MessageKey;
import com.vandunxg.trackee.common.util.LocalizationUtils;
import com.vandunxg.trackee.common.util.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j(topic = "AUTH-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    LocalizationUtils localizationUtils;
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        log.info("[POST /auth/register]={}", request);

        return ResponseUtil.success(
                localizationUtils.get(MessageKey.AUTH_REGISTER_SUCCESS),
                authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        log.info("[POST /auth/login]={}", request);

        return ResponseUtil.success(
                localizationUtils.get(MessageKey.AUTH_LOGIN_SUCCESS),
                authService.login(request, response));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestParam("token") String token, @RequestParam("userId") String userId) {
        log.info("[GET /auth/verify]");

        authService.verifyVerificationToken(token, userId);

        return ResponseUtil.success(localizationUtils.get(MessageKey.AUTH_VERIFICATION_SUCCESS));
    }
}
