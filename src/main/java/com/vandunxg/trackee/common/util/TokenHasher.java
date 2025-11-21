package com.vandunxg.trackee.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class TokenHasher {

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error hashing token", ex);
        }
    }

    public boolean matchToken(String rawToken, String storedHash) {
        return hashToken(rawToken).equals(storedHash);
    }
}
