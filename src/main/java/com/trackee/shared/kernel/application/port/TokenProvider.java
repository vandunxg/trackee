/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.application.port;

import com.trackee.shared.kernel.dto.AuthenticatedUser;

import java.util.UUID;

/**
 * @author vandunxg
 */
public interface TokenProvider {

    String generateEmailToken(UUID userId, String email);

    String validateEmailToken(String token);

    String generateAccessToken(AuthenticatedUser authenticatedUser, UUID userId);

    String generateRefreshToken(UUID userId);
}
