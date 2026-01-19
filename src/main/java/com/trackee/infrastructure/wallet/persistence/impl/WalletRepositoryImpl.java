/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence.impl;

import com.trackee.domain.wallet.Wallet;
import com.trackee.domain.wallet.repository.WalletRepository;
import com.trackee.infrastructure.common.persitence.AbstractDomainRepository;
import com.trackee.infrastructure.wallet.persistence.entity.WalletEntity;
import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Slf4j(topic = "WALLET-REPOSITORY")
public class WalletRepositoryImpl extends AbstractDomainRepository<Wallet, WalletEntity, UUID>
        implements WalletRepository {

    protected WalletRepositoryImpl(
            JpaRepository<WalletEntity, UUID> jpaRepository, PersistenceMapper<Wallet, WalletEntity> mapper) {
        super(jpaRepository, mapper);
    }
}
