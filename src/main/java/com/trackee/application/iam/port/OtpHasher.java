/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.port;

/**
 * @author vandunxg
 */
public interface OtpHasher {

    String hash(String rawOtp);

    boolean matches(String rawOtp, String hash);
}
