/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.application.port;

import java.util.UUID;

import com.trackee.shared.kernel.dto.AuthenticatedUser;

/**
 * @author vandunxg
 */
public interface TokenProvider {

    String generateAccessToken(AuthenticatedUser authenticatedUser, UUID userId);

    String generateRefreshToken(UUID userId);
}
