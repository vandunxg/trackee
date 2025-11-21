package com.vandunxg.trackee.users.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vandunxg.trackee.common.domain.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class VerificationToken extends BaseEntity {

    UUID userId;

    String otpHash;

    String tokenHash;

    String email;

    Instant expiryAt;

    Instant usedAt;

    public void markAsUsed() {
        this.usedAt = Instant.now();
    }
}
