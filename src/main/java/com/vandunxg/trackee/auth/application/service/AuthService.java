package com.vandunxg.trackee.auth.application.service;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.auth.api.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);
}
