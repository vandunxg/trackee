/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence.mapper;

import com.trackee.domain.iam.OtpCode;
import com.trackee.infrastructure.iam.persistence.entity.OtpCodeEntity;
import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface OtpCodePersistenceMapper extends PersistenceMapper<OtpCode, OtpCodeEntity> {}
