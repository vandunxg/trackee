package com.vandunxg.trackee.users.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vandunxg.trackee.common.domain.BaseEntity;
import com.vandunxg.trackee.common.enums.RoleType;
import com.vandunxg.trackee.common.enums.UserStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    String fullName;

    @Column(unique = true)
    String phoneNumber;

    @Column(nullable = false, unique = true)
    String email;

    Instant birthDate;

    @Column(nullable = false)
    String password;

    String avatarUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RoleType role;
}
