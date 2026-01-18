/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.usecase;

import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.shared.kernel.application.port.TokenProvider;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.web.iam.response.ForgetPasswordResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-VERIFY-FORGET-PASSWORD-OTP-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVerifyForgetPasswordUserCase {

  TokenProvider tokenProvider;
  OtpCodeRepository otpCodeRepository;
  UserRepository userRepository;

  public ForgetPasswordResponse verify(String code) {
    log.info("[verify]={}", code);

    OtpCode otpCode =
            otpCodeRepository
                    .findByHashedCode(code)
                    .orElseThrow(() -> new ResponseException(NotFoundError.OTP_CODE_NOT_FOUND));

    User user = findUserById(otpCode.getUserId());

    otpCode.verifyForgetPassword();

    List<OtpCode> otpCodes =
            otpCodeRepository.findAllOtpCodesNotUsedByUserId(user.getId(), OtpPurpose.FORGET_PASSWORD);

    otpCodes.forEach(OtpCode::revoked);
    otpCodes.add(otpCode);

    otpCodeRepository.saveAll(otpCodes);

    return new ForgetPasswordResponse(
            tokenProvider.generateEmailToken(user.getId(), user.getEmail()));
  }

  User findUserById(UUID userId) {
    log.info("[findUserById]={}", userId);

    return userRepository
            .findById(userId)
            .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));
  }
}
