/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackee.application.iam.usecase.UserActiveUseCase;
import com.trackee.application.iam.usecase.UserLoginUseCase;
import com.trackee.application.iam.usecase.UserRegisterUseCase;
import com.trackee.shared.kernel.web.Response;
import com.trackee.web.iam.request.ActiveRequest;
import com.trackee.web.iam.request.LoginRequest;
import com.trackee.web.iam.request.RegisterRequest;
import com.trackee.web.iam.response.LoginResponse;
import com.trackee.web.iam.response.UserRegisterResponse;

/**
 * @author vandunxg
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserRegisterUseCase userRegisterUseCase;
    UserLoginUseCase userLoginUseCase;
    UserActiveUseCase userActiveUseCase;

    @PostMapping("/register")
    public Response<UserRegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("[register]={}", request);

        return Response.of(userRegisterUseCase.register(request));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("[login]={}", request);

        return Response.of(userLoginUseCase.login(request));
    }

    @PostMapping("/active")
    public Response<LoginResponse> active(@Valid @RequestBody ActiveRequest request) {
        log.info("[active]={}", request);

        return Response.of(userActiveUseCase.active(request));
    }
}
