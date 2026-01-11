/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.persistence.entity;

import com.trackee.shared.infrastructure.persistence.AuditableEntity;
import com.trackee.shared.kernel.domain.enums.UserRole;
import com.trackee.shared.kernel.domain.enums.UserStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

/**
 * @author vandunxg
 */
@Entity
@Table(
        name = "users",
        indexes = {
            @Index(name = "user_email_idx", columnList = "email"),
            @Index(name = "user_deleted_at_idx", columnList = "deleted_at")
        })
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    UUID id;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "phone", unique = true, length = 20)
    String phone;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "birthday")
    Instant birthday;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Column(name = "last_login")
    Instant lastLogin;

    @Column(name = "deleted_at")
    Instant deletedAt;
}
