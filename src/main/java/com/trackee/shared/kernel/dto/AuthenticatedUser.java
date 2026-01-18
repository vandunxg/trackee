/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.dto;

import java.util.List;

public record AuthenticatedUser(String username, List<String> authorities) {}
