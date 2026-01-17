/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam.event;

import java.util.UUID;

public record UserResendOtpEvent(UUID userId, String mailTo, String fullName) {}
