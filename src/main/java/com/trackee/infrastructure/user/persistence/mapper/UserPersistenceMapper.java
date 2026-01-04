/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.user.persistence.mapper;

import com.trackee.domain.iam.User;
import com.trackee.infrastructure.user.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity userEntity);
}
