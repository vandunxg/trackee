/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.util;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * Production-ready code generator. - SecureRandom - Supports numeric OTP with leading zeros -
 * Supports custom charset (e.g. alphanumeric without ambiguous chars)
 *
 * @author vandunxg
 */
public final class CodeGenerator {

    private static final SecureRandom RNG = new SecureRandom();

    // Avoid ambiguous chars: O/0, I/1, l/1 ...
    public static final String ALPHANUMERIC_SAFE =
            "ABCDEFGHJKLMNPQRSTUVWXYZ" + "23456789" + "abcdefghjkmnpqrstuvwxyz";

    private CodeGenerator() {}

    /**
     * Generate numeric OTP with fixed length (keeps leading zeros). Example: length=6 -> "000042"
     * is possible.
     */
    public static String numeric(int length) {
        if (length <= 0 || length > 18) {
            throw new IllegalArgumentException("length must be between 1 and 18");
        }

        // Generate digits one-by-one to preserve leading zeros
        char[] out = new char[length];
        for (int i = 0; i < length; i++) {
            out[i] = (char) ('0' + RNG.nextInt(10));
        }
        return new String(out);
    }

    /**
     * Generate code from provided charset.
     */
    public static String fromCharset(int length, String charset) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be > 0");
        }
        Objects.requireNonNull(charset, "charset");
        if (charset.length() < 2) {
            throw new IllegalArgumentException("charset must have at least 2 chars");
        }

        char[] chars = charset.toCharArray();
        char[] out = new char[length];

        for (int i = 0; i < length; i++) {
            out[i] = chars[RNG.nextInt(chars.length)];
        }
        return new String(out);
    }

    /** Safe alphanumeric code (no ambiguous chars). */
    public static String alphanumericSafe(int length) {
        return fromCharset(length, ALPHANUMERIC_SAFE);
    }
}
