/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.command;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author vandunxg
 */
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterCommand {

    String email;
    String fullName;
    String passwordHash;
}
