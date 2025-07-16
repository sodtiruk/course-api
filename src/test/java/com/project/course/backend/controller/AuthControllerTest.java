package com.project.course.backend.controller;

import com.project.course.backend.common.dto.BaseResponse;
import com.project.course.backend.module.auth.controller.AuthController;
import com.project.course.backend.module.auth.dto.request.LoginRequest;
import com.project.course.backend.module.auth.dto.request.RegisterRequest;
import com.project.course.backend.module.auth.dto.response.LoginResponse;
import com.project.course.backend.module.auth.dto.response.RefreshTokenResponse;
import com.project.course.backend.module.auth.dto.response.RegisterResponse;
import com.project.course.backend.module.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthService authService;


    @Test
    void test_login_ShouldReturnSuccessResponse() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password123");
        LoginResponse loginResponse = new LoginResponse("token123");

        when(authService.login(response, loginRequest)).thenReturn(loginResponse);
        ResponseEntity<BaseResponse<LoginResponse>> result = authController.login(response, loginRequest);

        // Assert
        Assertions.assertNotNull(result.getBody());
        assertEquals(new LoginResponse("token123"), result.getBody().getData());
        assertEquals("success", result.getBody().getMessage());
        assertEquals(200, result.getBody().getStatusCode());
    }

    @Test
    void test_register_ShouldReturnSuccessResponse() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("newuser@gmail.com");
        registerRequest.setUsername("new_user");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setEmail("new_user");

        when(authService.register(registerRequest)).thenReturn(registerResponse);

        ResponseEntity<BaseResponse<RegisterResponse>> result = authController.register(registerRequest);
        // Assert
        Assertions.assertNotNull(result.getBody());
        assertEquals(new RegisterResponse("new_user"), result.getBody().getData());
        assertEquals("success", result.getBody().getMessage());
        assertEquals(200, result.getBody().getStatusCode());
    }

    @Test
    void test_refreshToken_ShouldReturnSuccessResponse() {
        // Arrange
        String refreshToken = "refreshToken123";

        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setNewAccessToken("newAccessToken123");

        when(authService.refreshToken(refreshToken, response)).thenReturn(refreshTokenResponse);

        ResponseEntity<BaseResponse<RefreshTokenResponse>> result = authController.refreshToken(refreshToken, response);

        // Assert
        Assertions.assertNotNull(result.getBody());
        assertEquals(new RefreshTokenResponse("newAccessToken123"), result.getBody().getData());
        assertEquals("success", result.getBody().getMessage());
        assertEquals(200, result.getBody().getStatusCode());
    }


}
