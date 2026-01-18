/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.application.mail;

import com.trackee.shared.kernel.dto.MailMessage;

/**
 * @author vandunxg
 */
public interface MailService {

    void sendMail(MailMessage message);
}
