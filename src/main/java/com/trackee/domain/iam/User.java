/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import com.trackee.application.iam.command.UserRegisterCommand;
import com.trackee.shared.exception.ResponseException;
import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.UserRole;
import com.trackee.shared.kernel.domain.enums.UserStatus;
import com.trackee.shared.kernel.exception.AuthenticationError;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author vandunxg
 */
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class User extends AuditableDomain {

    UUID id;
    String fullName;
    String phone;
    String email;
    Instant birthday;
    String passwordHash;
    UserStatus status;
    String avatarUrl;
    String avatarFileId;
    Boolean isFirstLogin;
    UserRole role;
    Instant deletedAt;

    public static User register(UserRegisterCommand cmd) {

        return User.builder()
                .id(UUID.randomUUID())
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .fullName(cmd.getFullName())
                .email(cmd.getEmail())
                .passwordHash(cmd.getPasswordHash())
                .build();
    }

    public void changePassword(String passwordHash) {

        this.passwordHash = passwordHash;
    }

    public void ensureActive() {

        if (Objects.equals(UserStatus.ACTIVE, this.status)) {
            throw new ResponseException(AuthenticationError.USER_NOT_ACTIVE);
        }
    }

    public void activeUser() {

        this.status = UserStatus.ACTIVE;
    }

    public void inactiveUser() {

        this.status = UserStatus.INACTIVE;
    }

    public void ensureUserActive() {

        if (!Objects.equals(UserStatus.ACTIVE, this.status)) {
            throw new ResponseException(AuthenticationError.USER_NOT_ACTIVE);
        }
    }
}
