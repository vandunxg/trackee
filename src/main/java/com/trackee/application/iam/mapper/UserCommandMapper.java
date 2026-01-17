/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.mapper;

import org.mapstruct.Mapper;

import com.trackee.application.iam.command.UserRegisterCommand;
import com.trackee.web.iam.request.RegisterRequest;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface UserCommandMapper {

    UserRegisterCommand toCommand(RegisterRequest request);
}
