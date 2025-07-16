package com.project.course.backend.common.exception;

import com.project.course.backend.common.dto.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            Exception.class,
    })
    public ResponseEntity<BaseErrorResponse> handleInternalServerError(Exception exception) {
        log.error(exception.getMessage(), exception);
        BaseErrorResponse body = BaseErrorResponse.builder()
                .message("Internal Server Error")
                .code(500)
                .build();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            CustomException.class,
    })
    public ResponseEntity<BaseErrorResponse> handleCustomException(CustomException exception) {
        log.error(exception.getMessage(), exception);
        BaseErrorResponse body = BaseErrorResponse.builder()
                .message(exception.getMessage())
                .code(400)
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }





}
