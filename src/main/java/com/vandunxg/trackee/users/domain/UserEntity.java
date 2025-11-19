package com.vandunxg.trackee.users.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vandunxg.trackee.common.domain.BaseEntity;
import com.vandunxg.trackee.common.enums.UserStatus;

@Getter
@Setter
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    UserStatus status;
}
