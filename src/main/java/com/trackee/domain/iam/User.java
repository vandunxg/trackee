/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.UserRole;
import com.trackee.shared.kernel.domain.enums.UserStatus;
import com.trackee.shared.kernel.exception.AuthenticationError;
import com.trackee.shared.kernel.exception.ResponseException;

/**
 * @author vandunxg
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
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

    User(UUID id, String fullName, String email, String passwordHash) {
        this.id = id;
        this.fullName = Objects.requireNonNull(fullName);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = UserStatus.INACTIVE;
        this.role = UserRole.USER;
        this.isFirstLogin = true;
    }

    public static User register(String fullName, String email, String passwordHash) {
        return new User(UUID.randomUUID(), fullName, email, passwordHash);
    }

    public void changePassword(String passwordHash) {

        this.passwordHash = passwordHash;
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

    public boolean isUserActive() {

        return UserStatus.ACTIVE.equals(this.status);
    }
}
