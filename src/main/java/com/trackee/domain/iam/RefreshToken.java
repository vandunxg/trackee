/* Copyright (c) 2026 Trackee */
package com.trackee.domain.iam;

import com.trackee.shared.kernel.domain.AuditableDomain;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
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
public class RefreshToken extends AuditableDomain {

    UUID id;
    UUID userId;
    String hashedToken;
    Instant expiresAt;
    Instant invokedAt;
    Instant replacedBy;
}
