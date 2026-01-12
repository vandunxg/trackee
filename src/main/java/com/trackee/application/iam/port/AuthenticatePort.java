/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.port;

import com.trackee.shared.kernel.dto.AuthenticatedUser;

/**
 * @author vandunxg
 */
public interface AuthenticatePort {

    AuthenticatedUser authenticate(String email, String password);
}
