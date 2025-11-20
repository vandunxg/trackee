package com.vandunxg.trackee.auth.application.service;

import jakarta.servlet.http.HttpServletResponse;

import com.vandunxg.trackee.auth.api.dto.LoginRequest;
import com.vandunxg.trackee.auth.api.dto.LoginResponse;
import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.auth.api.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request, HttpServletResponse response);
}
