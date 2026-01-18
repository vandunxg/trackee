/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam;

import com.trackee.application.iam.usecase.*;
import com.trackee.shared.kernel.web.Response;
import com.trackee.web.iam.request.*;
import com.trackee.web.iam.response.LoginResponse;
import com.trackee.web.iam.response.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    UserResendOtpUseCase userResendOtpUseCase;
    UserForgetPasswordUseCase userForgetPasswordUseCase;

    @PostMapping("/register")
    public Response<UserRegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("[register]={}", request.email());

        return Response.of(userRegisterUseCase.register(request));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("[login]={}", request.email());

        return Response.of(userLoginUseCase.login(request));
    }

    @PostMapping("/active")
    public Response<LoginResponse> active(@Valid @RequestBody ActiveRequest request) {
        log.info("[active]={}", request.email());

        return Response.of(userActiveUseCase.active(request));
    }

    @PostMapping("/resend")
    public Response<Boolean> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        log.info("[resendOtp]={}", request);

        return Response.of(userResendOtpUseCase.resend(request));
    }

    @PostMapping("/forget-password")
    public Response<Boolean> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        log.info("[forgetPassword]={}", request);

        return Response.of(userForgetPasswordUseCase.forgetPassword(request));
    }
}
