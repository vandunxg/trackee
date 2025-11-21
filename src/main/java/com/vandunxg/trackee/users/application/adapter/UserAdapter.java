package com.vandunxg.trackee.users.application.adapter;

import java.util.UUID;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.users.application.adapter.dto.UserInfoDTO;

public interface UserAdapter {

    UUID createdUser(RegisterRequest request);

    UserInfoDTO getUserInfo(UUID id);
}
