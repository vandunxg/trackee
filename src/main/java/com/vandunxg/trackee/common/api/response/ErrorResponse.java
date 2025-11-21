package com.vandunxg.trackee.common.api.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse implements Serializable {

    @Builder.Default boolean success = false;

    @Builder.Default Instant timestamp = Instant.now();

    String code;
    int status;
    String message;
    String error;
    String path;

    Map<String, String> errors;
}
