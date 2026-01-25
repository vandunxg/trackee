/* Copyright (c) 2026 Trackee */
package com.trackee.web.wallet.request;

import com.trackee.shared.kernel.domain.enums.Currency;
import com.trackee.shared.kernel.domain.enums.WalletType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateWalletRequest(
        @NotBlank(message = "{WALLET_NAME_REQUIRED}") @Size(max = 100, message = "{WALLET_NAME_MAX_LENGTH}")
        String name,

        @NotNull(message = "{BALANCE_REQUIRED}")
        @DecimalMin(value = "0.0", inclusive = true, message = "{BALANCE_MUST_BE_POSITIVE}")
        @Digits(integer = 18, fraction = 2, message = "{BALANCE_INVALID_FORMAT}")
        BigDecimal balance,

        @NotNull(message = "{IS_DEFAULT_REQUIRED}") Boolean isDefault,

        @NotNull(message = "{IS_TOTAL_IGNORED_REQUIRED}") Boolean isTotalIgnored,

        @NotNull(message = "{WALLET_TYPE_REQUIRED}") WalletType walletType,

        @NotNull(message = "{CURRENCY_REQUIRED}") Currency currency) {
}
