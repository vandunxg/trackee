/* Copyright (c) 2026 Trackee */
package com.trackee.application.wallet.usecase;

import com.trackee.domain.wallet.Wallet;
import com.trackee.domain.wallet.repository.WalletRepository;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.shared.kernel.web.mapper.AutoResponseMapper;
import com.trackee.web.wallet.request.UpdateWalletRequest;
import com.trackee.web.wallet.response.WalletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "CREATE-WALLET-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateWalletUseCase {

    WalletRepository walletRepository;
    AutoResponseMapper autoResponseMapper;

    public WalletResponse update(UUID walletId, UpdateWalletRequest request) {
        log.info("[updateWallet]={}", request);

        Wallet wallet = getWalletById(walletId);

        wallet.updateWallet(
                request.name(),
                request.balance(),
                request.description(),
                request.isDefault(),
                request.isTotalIgnored(),
                request.walletType(),
                request.currency());

        walletRepository.save(wallet);

        return autoResponseMapper.toWalletResponse(wallet);
    }

    Wallet getWalletById(UUID walletId) {
        log.info("[getWalletById]={}", walletId);

        return walletRepository
                .findById(walletId)
                .orElseThrow(() -> new ResponseException(NotFoundError.WALLET_NOT_FOUND, walletId));
    }
}
