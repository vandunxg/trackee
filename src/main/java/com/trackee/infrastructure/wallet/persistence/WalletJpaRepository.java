/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence;

import com.trackee.infrastructure.wallet.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {

    @Query("""
                from WalletEntity w where w.deletedAt is null and w.id = :walletId and w.userId = :userId
            """)
    Optional<WalletEntity> findWalletByIdAndUserId(@Param("walletId") UUID walletId, @Param("userId") UUID userId);
}
