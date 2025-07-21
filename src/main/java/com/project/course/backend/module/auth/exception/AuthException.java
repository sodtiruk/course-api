package com.project.course.backend.module.auth.exception;

import com.project.course.backend.common.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(String message) {
        super(message);
    }

    public static AuthException invalidUser() {
        return new AuthException("Invalid email or password");
    }

}
