/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.application.iam.command.UserRegisterCommand;
import com.trackee.application.iam.mapper.UserCommandMapper;
import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.web.iam.request.RegisterRequest;
import com.trackee.web.iam.response.UserRegisterResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-REGISTER-USE-CASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisterUseCase {

    UserRepository userRepository;
    UserCommandMapper userCommandMapper;
    PasswordEncoder passwordEncoder;

    public UserRegisterResponse handle(RegisterRequest request) {
        log.info("[handle]={}", request);

        UserRegisterCommand cmd = userCommandMapper.toCommand(request);
        cmd.setPasswordHash(passwordEncoder.encode(request.password()));

        User user = User.register(cmd);

        userRepository.save(user);

        return new UserRegisterResponse(user.getId());
    }
}
