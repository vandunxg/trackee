/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author vandunxg
 */
public interface Constants {

    String ROLE_PREFIX = "ROLE_";

    String CONTENT_KEY = "content";

    interface RegexPattern {
        String PLATFORM_REGEX = "^(WEB|IOS|ANDROID)$";
        String PASSWORD_REGEX =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    }

    interface CodeGenerator {
        int OTP_CODE_LENGTH = 6;
    }

    interface System {
        String SYSTEM = "SYSTEM";
        String ANONYMOUS_USER = "anonymousUser";
    }

    interface JwtConstant {
        String ANONYMOUS_ACCOUNT = "anonymous";
        String HTTP = "http://";
        String HTTPS = "https://";
        String ACB_CACHE_TOKEN_NAME = "acbAuth_token";
        List<String> EXTENSIONS = Arrays.asList("bmp,jpg,png,jpeg".split(","));
        String AUTHORITY_TYPE = "auth_type";
        String USER_ID_CLAIM = "user_id";
        String ROLE_CLAIM = "role";
        String EMAIL_CLAIM = "email";
        String API_TOKEN = "Api-token";
        String CLIENT = "client";
        String REFRESH_TOKEN = "refresh_token";
        String CLIENT_AUTHORITY = "client";
        String CLIENT_MESSAGE_ID = "client_message_id";
        String CLIENT_ID = "client-id";
        String CLIENT_SECRET = "client-secret";
        String REMOTE_IP = "remote_ip";
        String API_DOC = "custom_api_doc";
        String EXCEPTION_MESSAGE = "custom_exception_message";
    }
}
