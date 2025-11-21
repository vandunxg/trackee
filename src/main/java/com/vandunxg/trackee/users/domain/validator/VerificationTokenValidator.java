package com.vandunxg.trackee.users.domain.validator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.users.domain.VerificationToken;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenValidator {

    public void validateToken(VerificationToken token, UUID userId) {

        if (token == null) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_NOT_FOUND);
        }

        if (token.getUsedAt() != null) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_USED);
        }
        if (!token.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_USER_MISMATCH);
        }

        if (token.getExpiryAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_EXPIRED);
        }

        if (token.getTokenHash() == null || token.getTokenHash().isBlank()) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_FORMAT_INVALID);
        }

        if (token.getOtpHash() != null && token.getOtpHash().isBlank()) {
            throw new BusinessException(ErrorCode.AUTH_VERIFICATION_TOKEN_INVALID);
        }
    }
}
