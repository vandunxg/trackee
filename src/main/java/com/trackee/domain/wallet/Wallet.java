/* Copyright (c) 2026 Trackee */
package com.trackee.domain.wallet;

import com.trackee.shared.kernel.domain.AuditableDomain;
import com.trackee.shared.kernel.domain.enums.Currency;
import com.trackee.shared.kernel.domain.enums.WalletType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Getter
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet extends AuditableDomain {

    UUID id;
    UUID userId;
    String name;
    BigDecimal balance;
    String description;
    boolean isDefault;
    boolean isTotalIgnored;
    WalletType walletType;
    Currency currency;
}
