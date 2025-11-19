package com.vandunxg.trackee.common.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties("jwt")
public class JwtProperties {

    String accessTokenSecret;

    String refreshTokenSecret;

    long accessTokenExpiration;

    long refreshTokenExpiration;
}
