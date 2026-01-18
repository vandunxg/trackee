/* Copyright (c) 2026 Trackee */
package com.trackee.web.iam.request;

import java.io.Serializable;

public record RegisterRequest(String email, String fullName, String password, String deviceId)
        implements Serializable {
}
