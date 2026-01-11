/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.dto;

import com.trackee.shared.kernel.domain.enums.MailPurpose;
import com.trackee.shared.kernel.util.Constants;
import java.util.List;
import java.util.Map;

public record MailMessage(
        List<String> to, String subject, MailPurpose mailPurpose, Map<String, Object> variables) {

    public MailMessage {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Recipients must not be empty");
        }
    }

    public static MailMessage simple(String to, String subject, String content) {

        return new MailMessage(List.of(to), subject, null, Map.of(Constants.CONTENT_KEY, content));
    }

    public static MailMessage template(
            String to, MailPurpose mailPurpose, Map<String, Object> variables) {

        return new MailMessage(List.of(to), null, mailPurpose, variables);
    }
}
