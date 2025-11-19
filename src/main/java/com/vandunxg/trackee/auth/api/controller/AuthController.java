package com.vandunxg.trackee.auth.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vandunxg.trackee.auth.api.controller.dto.RegisterRequest;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        log.info("[POST /auth/register]={}", request);

        return ResponseUtil.success(localizationUtils.get(MessageKey.AUTH_REGISTER_SUCCESS), null);
    }
}
