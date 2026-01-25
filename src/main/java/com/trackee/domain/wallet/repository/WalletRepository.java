/* Copyright (c) 2026 Trackee */
package com.trackee.domain.wallet.repository;

import com.trackee.domain.wallet.Wallet;
import com.trackee.shared.kernel.domain.repository.DomainRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
public interface WalletRepository extends DomainRepository<Wallet, UUID> {

    Optional<Wallet> findWalletByIdAndUserId(UUID walletId, UUID userId);
}
