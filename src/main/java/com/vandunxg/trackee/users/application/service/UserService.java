package com.vandunxg.trackee.users.application.service;

import java.util.UUID;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;

public interface UserService {

    UUID createUserRegistration(RegisterRequest request);
}
