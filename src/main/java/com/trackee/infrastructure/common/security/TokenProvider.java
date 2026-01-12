/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.trackee.shared.kernel.dto.AuthenticatedUser;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.util.Constants;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.trackee.shared.kernel.util.Constants.JwtConstant.*;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "TOKEN-PROVIDER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String EXPIRED_JWT_TOKEN = "Expired JWT token.";
    private static final String INVALID_JWT_SIGNATURE = "Invalid JWT signature.";

    JwtProperties jwtProperties;
    private KeyPair keyPair;

    public JWKSet jwkSet() {

        RSAKey.Builder builder =
                new RSAKey.Builder((RSAPublicKey) this.keyPair.getPublic())
                        .keyUse(KeyUse.SIGNATURE)
                        .algorithm(JWSAlgorithm.RS256)
                        .keyID(UUID.randomUUID().toString());
        return new JWKSet(builder.build());
    }

    public String createToken(AuthenticatedUser authenticatedUser, String userId) {

        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenExpiresIn());

        return Jwts.builder()
                .subject(authenticatedUser.username())
                .claim(USER_ID_CLAIM, userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public String createRefreshToken(String userId) {

        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenExpiresIn());

        return Jwts.builder()
                .subject(userId)
                .claim(AUTHORITY_TYPE, REFRESH_TOKEN)
                .signWith(keyPair.getPrivate())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .compact();
    }

    public String createRefreshTokenRememberMe(String userId) {

        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenExpiresIn());

        return Jwts.builder()
                .subject(userId)
                .claim(AUTHORITY_TYPE, REFRESH_TOKEN)
                .signWith(keyPair.getPrivate())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .compact();
    }

    public String getSubject(String token) {
        Claims claims =
                Jwts.parser()
                        .verifyWith(keyPair.getPublic())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return claims.getSubject();
    }

    public Claims parse(String token) {
        try {

            return Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.info(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }

        return null;
    }

    public String createTokenSendEmail(String userId, String email) {

        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenExpiresIn());

        return Jwts.builder()
                .subject(userId)
                .claim(EMAIL_CLAIM, email)
                .signWith(keyPair.getPrivate())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .compact();
    }

    public String createTokenChangeEmail(String userId, String email) {
        long now = Instant.now().toEpochMilli();
        Date validity = new Date(now + Duration.ofMinutes(5).toMillis());

        return Jwts.builder()
                .subject(userId)
                .claim(Constants.JwtConstant.EMAIL_CLAIM, email)
                .signWith(keyPair.getPrivate())
                .issuedAt(new Date(now))
                .expiration(validity)
                .compact();
    }

    public boolean trustToken(String authToken) {
        try {

            Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(authToken);

            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.info(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }

    public boolean validateToken(String authToken) {
        try {

            Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(authToken);

        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.info(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return true;
    }

    public String validateEmailToken(String authToken) {
        try {

            Claims claims =
                    Jwts.parser()
                            .verifyWith(keyPair.getPublic())
                            .build()
                            .parseSignedClaims(authToken)
                            .getPayload();

            return claims.getSubject();
        } catch (ExpiredJwtException exception) {
            log.info(EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            log.warn(INVALID_JWT_SIGNATURE, e);
        }
        return null;
    }

    public boolean validateRefreshToken(String authToken) {
        try {
            Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(authToken);
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.info(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return true;
    }

    public boolean isInvalidRefreshToken(String authToken) {
        return true;
    }

    public void trustRefreshToken(String refreshToken) {
        try {
            Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(refreshToken);
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info(INVALID_JWT_SIGNATURE);
            throw new com.trackee.shared.exception.ResponseException(
                    AuthenticationError.INVALID_JWT_SIGNATURE_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info(EXPIRED_JWT_TOKEN);
            throw new com.trackee.shared.exception.ResponseException(
                    AuthenticationError.EXPIRED_REFRESH_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
    }

    /**
     * resolve token from request
     *
     * @param request: HttpServletRequest
     * @return: String
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }

        return null;
    }
}
