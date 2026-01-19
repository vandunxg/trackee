/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import com.trackee.application.iam.port.OtpHasher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "OTP-HASHER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HmacOtpHasher implements OtpHasher {

    static String ALGORITHM = "HmacSHA256";
    static HexFormat HEX = HexFormat.of();

    Mac mac;

    @Autowired
    public HmacOtpHasher(OtpProperties properties) {
        try {
            this.mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(properties.secret().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            this.mac.init(keySpec);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot init HMAC OTP hasher", e);
        }
    }

    @Override
    public String hash(String rawOtp) {
        log.info("[hash]");

        byte[] hmac = mac.doFinal(rawOtp.getBytes(StandardCharsets.UTF_8));
        return HEX.formatHex(hmac);
    }

    @Override
    public boolean matches(String rawOtp, String hash) {
        log.info("[matches]");

        String computed = hash(rawOtp);
        return MessageDigest.isEqual(computed.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
    }
}
