package com.vandunxg.trackee.users.application.adapter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.users.application.adapter.dto.UserInfoDTO;
import com.vandunxg.trackee.users.application.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-ADAPTER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAdapterImpl implements UserAdapter {

    UserService userService;

    @Override
    public UUID createdUser(RegisterRequest request) {
        log.info("[createdUser] request={}", request);

        return userService.createUserRegistration(request);
    }

    @Override
    public UserInfoDTO getUserInfo(UUID userId) {
        log.info("[getUserInfo] userId={}", userId);

        return userService.getUserInfo(userId);
    }

    @Override
    public void activeUser(UUID userId) {
        log.info("[activeUser] userId={}", userId);

        userService.activeUser(userId);
    }
}
