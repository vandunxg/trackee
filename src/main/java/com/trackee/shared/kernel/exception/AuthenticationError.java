package com.trackee.shared.kernel.exception;

import lombok.Getter;

/**
 * @author vandunxg
 */
@Getter
public enum AuthenticationError implements ResponseError {
    UNKNOWN(40100001, "UNKNOWN"),
    UNAUTHORISED(40100002, "Unauthorised"),
    FORBIDDEN_ACCESS_TOKEN(
            40100003, "Access token has been forbidden due to user has logged out or deactivated"),
    FORBIDDEN_REFRESH_TOKEN(40100004, "Refresh token has been forbidden"),
    INVALID_REFRESH_TOKEN(40100005, "Refresh token has been forbidden"),
    VALIDATE_EXPIRATION_TIME(40100006, "validate expiration time"),
    VALIDATE_TOKEN_ID(40100007, "JWT Token is not an ID Token"),
    VALIDATE_ISSUER(40100008, "Issuer does not match idp"),
    ONLY_CLIENT_ACCESS_RESOURCE(40100009, "Only client can access this resource"),
    INVALID_API_TOKEN(40100010, "Api token is invalid"),
    INVALID_JWT_SIGNATURE_REFRESH_TOKEN(40100011, "Refresh token is invalid signature"),
    EXPIRED_REFRESH_TOKEN(40100012, "Refresh token is expired"),
    REFRESH_TOKEN_WAS_REVOKE(40100013, "Refresh token was revoke"),
    INVALID_TOKEN_TYPE(40100014, "Invalid token type"),
    INVALID_CREDENTIALS(40100015, "Invalid credentials"),
    USER_NOT_ACTIVE(40100016, "User not active");

    private final Integer code;
    private final String message;

    AuthenticationError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
