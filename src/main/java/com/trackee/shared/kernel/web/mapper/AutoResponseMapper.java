/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.web.mapper;

import com.trackee.domain.wallet.Wallet;
import com.trackee.web.wallet.response.WalletResponse;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface AutoResponseMapper {

    WalletResponse toWalletResponse(Wallet wallet);
}
