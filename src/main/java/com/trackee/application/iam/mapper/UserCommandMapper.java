/* Copyright (c) 2026 Trackee */
package com.trackee.application.iam.mapper;

import com.trackee.application.iam.command.UserRegisterCommand;
import com.trackee.web.iam.request.RegisterRequest;
import org.mapstruct.Mapper;

/**
 * @author vandunxg
 */
@Mapper(componentModel = "spring")
public interface UserCommandMapper {

    UserRegisterCommand toCommand(RegisterRequest request);
}
