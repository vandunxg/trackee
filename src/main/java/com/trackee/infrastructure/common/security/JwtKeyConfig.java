/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author vandunxg
 */
@Configuration
public class JwtKeyConfig {

    @Bean
    public KeyPair jwtKeyPair(JwtProperties props) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(
                new ClassPathResource(props.keyStore()).getInputStream(),
                props.keyStorePassword().toCharArray());

        PrivateKey privateKey =
                (PrivateKey) ks.getKey(props.keyAlias(), props.keyStorePassword().toCharArray());

        PublicKey publicKey = ks.getCertificate(props.keyAlias()).getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }
}
