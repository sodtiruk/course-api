package com.project.course.backend.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseErrorResponse {

    private String message;
    private Integer code;

}
