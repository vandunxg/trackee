/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence.entity;

import com.trackee.shared.infrastructure.persistence.AuditableEntity;
import com.trackee.shared.kernel.domain.enums.Currency;
import com.trackee.shared.kernel.domain.enums.WalletType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Entity
@Table(
        name = "wallets",
        indexes = {
                @Index(name = "wallet_user_id_idx", columnList = "user_id"),
                @Index(name = "wallet_deleted_at_idx", columnList = "deleted_at")
        })
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WalletEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    UUID id;

    @Column(name = "user_id", nullable = false)
    UUID userId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "balance", nullable = false)
    BigDecimal balance;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @Column(name = "is_default")
    boolean isDefault;

    @Column(name = "is_total_ignored")
    boolean isTotalIgnored;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type")
    WalletType walletType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    Currency currency;

    @Column(name = "deleted_at")
    Instant deletedAt;
}
