package com.project.course.backend.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {

    private String message;
    private Integer statusCode;
    private T data;

}
