package com.vandunxg.trackee.users.application.adapter.dto;

import java.util.UUID;

public record UserInfoDTO(UUID userId, String email, String fullName, String avatarUrl) {}
