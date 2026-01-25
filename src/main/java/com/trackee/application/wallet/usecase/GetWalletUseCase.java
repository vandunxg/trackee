/* Copyright (c) 2026 Trackee */
package com.trackee.application.wallet.usecase;

import com.trackee.domain.iam.User;
import com.trackee.domain.iam.repository.UserRepository;
import com.trackee.domain.wallet.Wallet;
import com.trackee.domain.wallet.repository.WalletRepository;
import com.trackee.shared.kernel.exception.NotFoundError;
import com.trackee.shared.kernel.exception.ResponseException;
import com.trackee.shared.kernel.web.mapper.AutoResponseMapper;
import com.trackee.web.wallet.response.WalletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vandunxg
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "CREATE-WALLET-USECASE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetWalletUseCase {

    UserRepository userRepository;
    WalletRepository walletRepository;
    AutoResponseMapper autoResponseMapper;

    public WalletResponse getById(UUID walletId) {
        log.info("[getById]={}", walletId);

        UUID authenticatedUserId = getAuthenticatedUserId();

        Wallet wallet = getWalletByIdAndUserId(walletId, authenticatedUserId);

        return autoResponseMapper.toWalletResponse(wallet);
    }

    UUID getAuthenticatedUserId() {
        log.info("[getAuthenticatedUserId]");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        return user.getId();
    }

    Wallet getWalletByIdAndUserId(UUID walletId, UUID userId) {
        log.info("[getWalletByIdAndUserId] walletId={} userId={}", walletId, userId);

        return walletRepository
                .findWalletByIdAndUserId(walletId, userId)
                .orElseThrow(() -> new ResponseException(NotFoundError.WALLET_NOT_FOUND, walletId));
    }
}
