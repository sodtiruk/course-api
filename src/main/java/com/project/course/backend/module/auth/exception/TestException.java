package com.project.course.backend.module.auth.exception;

import com.project.course.backend.common.exception.CustomException;


public class TestException extends CustomException {

    public TestException(String message) {
        super(message);
    }

    public TestException invalidUser() {
        return new TestException("Invalid user");
    }

}
