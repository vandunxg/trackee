/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.event;

import com.trackee.application.iam.command.OtpGenerateCmd;
import com.trackee.application.iam.port.OtpHasher;
import com.trackee.domain.iam.OtpCode;
import com.trackee.domain.iam.event.UserRegisterEvent;
import com.trackee.domain.iam.repository.OtpCodeRepository;
import com.trackee.shared.kernel.application.mail.MailService;
import com.trackee.shared.kernel.domain.enums.MailPurpose;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import com.trackee.shared.kernel.dto.MailMessage;
import com.trackee.shared.kernel.util.CodeGenerator;
import com.trackee.shared.kernel.util.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vandunxg
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "USER-EVENT-HANDLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventHandler {

    OtpHasher otpHasher;
    MailService mailService;
    OtpCodeRepository otpCodeRepository;

    @Async("virtualThreadExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegisterEvent(UserRegisterEvent event) {
        log.info("[onUserRegisterEvent]={}", event);

        String code = CodeGenerator.numeric(Constants.CodeGenerator.OTP_CODE_LENGTH);

        OtpGenerateCmd cmd = new OtpGenerateCmd(code, event.userId(), OtpPurpose.REGISTER);

        OtpCode otpCode = new OtpCode(cmd, otpHasher);

        Map<String, Object> variables = new HashMap<>();

        variables.put("full_name", event.fullName());
        variables.put("otp", code);

        MailMessage message = MailMessage.template(event.mailTo(), MailPurpose.REGISTER, variables);

        otpCodeRepository.save(otpCode);

        mailService.sendMail(message);
    }
}
