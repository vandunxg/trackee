/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence;

import com.trackee.infrastructure.wallet.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vandunxg
 */
@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {
}
