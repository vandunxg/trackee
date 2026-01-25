/* Copyright (c) 2026 Trackee */
package com.trackee.web.wallet.request;

import com.trackee.shared.kernel.domain.enums.Currency;
import com.trackee.shared.kernel.domain.enums.WalletType;

import java.math.BigDecimal;

public record UpdateWalletRequest(
        String name,
        BigDecimal balance,
        String description,
        Boolean isDefault,
        Boolean isTotalIgnored,
        WalletType walletType,
        Currency currency) {
}
