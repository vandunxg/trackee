package com.vandunxg.trackee.users.application.adapter;

import java.util.UUID;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;

public interface UserAdapter {

    UUID createdUser(RegisterRequest request);
}
