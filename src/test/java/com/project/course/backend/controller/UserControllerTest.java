package com.project.course.backend.controller;

import com.project.course.backend.module.auth.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUserShouldReturnUser() {
        assertEquals("User details", userController.getUser());
    }


}
