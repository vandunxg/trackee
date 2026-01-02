package com.trackee.domain.iam;

import com.trackee.domain.iam.enums.UserRole;
import com.trackee.domain.iam.enums.UserStatus;
import com.trackee.shared.exception.ResponseException;
import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.exception.AuthenticationError;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class User extends AuditableDomain {

    UUID id;
    String fullName;
    String phone;
    String email;
    Instant birthday;
    String passwordHash;
    UserStatus status;
    Boolean deleted;
    String avatarUrl;
    String avatarFileId;
    Boolean isFirstLogin;
    UserRole role;

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
}
