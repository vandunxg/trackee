/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence.mapper;

import com.trackee.domain.iam.User;
import com.trackee.infrastructure.iam.persistence.entity.UserEntity;
import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface UserPersistenceMapper extends PersistenceMapper<User, UserEntity> {}
