/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.wallet.persistence.mapper;

import com.trackee.domain.wallet.Wallet;
import com.trackee.infrastructure.wallet.persistence.entity.WalletEntity;
import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface WalletPersistenceMapper extends PersistenceMapper<Wallet, WalletEntity> {
}
