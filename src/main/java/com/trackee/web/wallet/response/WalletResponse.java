/* Copyright (c) 2026 Trackee */
package com.trackee.web.wallet.response;

import com.trackee.shared.kernel.domain.enums.Currency;
import com.trackee.shared.kernel.domain.enums.WalletType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author vandunxg
 */
public record WalletResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal balance,
        String description,
        boolean isDefault,
        boolean isTotalIgnored,
        WalletType walletType,
        Currency currency) {
}
