package com.vandunxg.trackee.common.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationCodeGenerator {

    SecureRandom secureRandom;
    static Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public VerificationCodeGenerator() {
        this.secureRandom = createSecureRandom();
    }

    public String generateOtp(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("OTP length must be >= 4");
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }

        return sb.toString();
    }

    public String generateOtp() {
        return generateOtp(6);
    }

    public String generateVerificationToken(int byteLength) {
        if (byteLength < 16) {
            throw new IllegalArgumentException("Token must be >= 16 bytes");
        }

        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);

        return base64UrlEncoder.encodeToString(randomBytes);
    }

    public String generateVerificationToken() {
        return generateVerificationToken(32);
    }

    public VerificationPair generateBoth() {
        return new VerificationPair(generateOtp(6), generateVerificationToken(32));
    }

    public VerificationPair generateBoth(int otpLength, int tokenBytes) {
        return new VerificationPair(generateOtp(otpLength), generateVerificationToken(tokenBytes));
    }

    public record VerificationPair(String otp, String token) {}

    SecureRandom createSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            return new SecureRandom();
        }
    }
}
