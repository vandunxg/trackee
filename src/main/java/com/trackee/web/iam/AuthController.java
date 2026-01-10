/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam;

import com.trackee.application.iam.usecase.UserRegisterUseCase;
import com.trackee.shared.kernel.web.Response;
import com.trackee.web.iam.request.RegisterRequest;
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

    @PostMapping("/register")
    public Response<UserRegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("[register]={}", request);

        return Response.of(userRegisterUseCase.handle(request));
    }
}
