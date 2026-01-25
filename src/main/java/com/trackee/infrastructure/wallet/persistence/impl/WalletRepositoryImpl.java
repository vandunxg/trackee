/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence.impl;

import com.trackee.domain.wallet.Wallet;
import com.trackee.domain.wallet.repository.WalletRepository;
import com.trackee.infrastructure.common.persitence.AbstractDomainRepository;
import com.trackee.infrastructure.wallet.persistence.WalletJpaRepository;
import com.trackee.infrastructure.wallet.persistence.entity.WalletEntity;
import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Repository
@Slf4j(topic = "WALLET-REPOSITORY")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletRepositoryImpl extends AbstractDomainRepository<Wallet, WalletEntity, UUID>
        implements WalletRepository {

    WalletJpaRepository walletJpaRepository;

    protected WalletRepositoryImpl(
            JpaRepository<WalletEntity, UUID> jpaRepository,
            PersistenceMapper<Wallet, WalletEntity> mapper,
            WalletJpaRepository walletJpaRepository) {
        super(jpaRepository, mapper);
        this.walletJpaRepository = walletJpaRepository;
    }

    @Override
    public Optional<Wallet> findWalletByIdAndUserId(UUID walletId, UUID userId) {
        log.info("[findWalletByIdAndUserId] walletId={} userId={}", walletId, userId);

        return walletJpaRepository.findWalletByIdAndUserId(walletId, userId).map(mapper::toDomain);
    }

    @Override
    public List<Wallet> findAllWalletByIdAndUserId(UUID userId) {
        log.info("[findAllWalletByIdAndUserId] userId={}", userId);

        return walletJpaRepository.findAllWalletByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
