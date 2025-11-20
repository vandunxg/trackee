package com.vandunxg.trackee.common.constant;

public class MessageKey {

    /** COMMON ERRORS * */
    public static final String VALIDATION_ERROR = "common.validation.error";

    public static final String INTERNAL_SERVER_ERROR = "common.internal.server.error";
    public static final String REQUIRED_REQUEST_BODY = "common.required.request.body";

    /** COMMON CRUD * */
    public static final String CREATED_SUCCESS = "common.created.success";

    public static final String UPDATED_SUCCESS = "common.updated.success";
    public static final String DELETED_SUCCESS = "common.deleted.success";
    public static final String NOT_FOUND = "common.not.found";
    public static final String INVALID_INPUT = "common.invalid.input";

    /** AUTH * */
    public static final String AUTH_LOGIN_SUCCESS = "auth.login.success";

    public static final String AUTH_LOGIN_FAIL = "auth.login.fail";
    public static final String AUTH_REGISTER_SUCCESS = "auth.register.success";
    public static final String AUTH_TOKEN_INVALID = "auth.token.invalid";
    public static final String AUTH_UNAUTHORIZED = "auth.unauthorized";
    public static final String AUTH_INVALID_CREDENTIALS = "auth.invalid.credentials";

    /** USER * */
    public static final String USER_NOT_FOUND = "user.not.found";

    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";
    public static final String USER_EMAIL_EXISTS = "user.email.exists";
    public static final String USER_INACTIVE = "user.inactive";

    /** USER VALIDATION * */
    public static final String EMAIL_BLANK = "email.blank";

    public static final String EMAIL_INVALID = "email.invalid";

    public static final String PASSWORD_BLANK = "password.blank";
    public static final String PASSWORD_INVALID = "password.invalid";
    public static final String PASSWORD_MISMATCH = "password.mismatch";
    public static final String PASSWORD_TOO_SHORT = "password.too.short";
    public static final String PASSWORD_TOO_LONG = "password.too.long";
    public static final String PASSWORD_TOO_WEAK = "password.too.weak";
    public static final String PASSWORD_NO_DIGIT = "password.no.digit";
    public static final String PASSWORD_NO_UPPERCASE = "password.no.uppercase";
    public static final String PASSWORD_NO_LOWERCASE = "password.no.lowercase";
    public static final String PASSWORD_NO_SPECIAL_CHAR = "password.no.special";
}
