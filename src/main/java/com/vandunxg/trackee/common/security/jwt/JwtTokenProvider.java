package com.vandunxg.trackee.common.security.jwt;

import static com.vandunxg.trackee.common.enums.TokenType.ACCESS_TOKEN;
import static com.vandunxg.trackee.common.enums.TokenType.REFRESH_TOKEN;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.vandunxg.trackee.common.enums.TokenType;
import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.common.security.principal.UserPrincipal;
import com.vandunxg.trackee.users.domain.UserEntity;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT-TOKEN-PROVIDER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenProvider {

    @NonFinal
    @Value("${jwt.private-key.access-token}")
    String ACCESS_TOKEN_PRIVATE_KEY;

    @NonFinal
    @Value("${jwt.private-key.refresh-token}")
    String REFRESH_TOKEN_PRIVATE_KEY;

    @NonFinal
    @Value("${jwt.expiration.access-token}")
    String ACCESS_TOKEN_EXPIRY_TIME;

    @NonFinal
    @Value("${jwt.expiration.refresh-token}")
    String REFRESH_TOKEN_EXPIRY_TIME;

    public String generateAccessToken(UserPrincipal userPrincipal) {
        log.info(
                "generate access token for user {} with authorities {}",
                userPrincipal.getUsername(),
                userPrincipal.getAuthorities());

        UserEntity user = userPrincipal.getUser();
        List<String> roles =
                userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles", roles);

        return generateAccessToken(claims, userPrincipal.getUsername());
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        log.info(
                "generate refresh token for user {} with authorities {}",
                userPrincipal.getUsername(),
                userPrincipal.getAuthorities());

        UserEntity user = userPrincipal.getUser();
        List<String> roles =
                userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles", roles);

        return generateRefreshToken(claims, userPrincipal.getUsername());
    }

    public String extractEmail(String token, TokenType type) {
        log.info("extractEmail");

        return extractClaim(token, type, Claims::getSubject);
    }

    public Date extractExpiration(String token, TokenType type) {
        log.info("[extractExpiration] token={} type={}", token.substring(0, 10), type);

        return extractClaim(token, type, Claims::getExpiration);
    }

    String generateAccessToken(Map<String, Object> claims, String email) {
        log.info("----------[ GENERATE-ACCESS-TOKEN ]----------");

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + Long.parseLong(ACCESS_TOKEN_EXPIRY_TIME)))
                .signWith(getKeys(ACCESS_TOKEN))
                .compact();
    }

    String generateRefreshToken(Map<String, Object> claims, String email) {
        log.info("----------[ GENERATE-REFRESH-TOKEN ]----------");

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + Long.parseLong(REFRESH_TOKEN_EXPIRY_TIME)))
                .signWith(getKeys(REFRESH_TOKEN))
                .compact();
    }

    Key getKeys(TokenType type) {
        log.info("----------[ GET-KEY ]----------");

        return switch (type) {
            case ACCESS_TOKEN -> Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(ACCESS_TOKEN_PRIVATE_KEY));
            case REFRESH_TOKEN -> Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(REFRESH_TOKEN_PRIVATE_KEY));
        };
    }

    <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        log.info("----------[ extractClaim ]----------");

        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    Claims extractAllClaims(String token, TokenType type) {
        log.info("----------[ EXTRACT-ALL-CLAIMS ]----------");

        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getKeys(type))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("[extractAllClaims] Invalid token type={} msg={}", type, e.getMessage());
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("[extractAllClaims] Unexpected error={}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
