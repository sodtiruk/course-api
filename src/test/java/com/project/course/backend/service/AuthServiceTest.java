package com.project.course.backend.service;

import com.project.course.backend.common.component.DtoEntityMapperTest;
import com.project.course.backend.module.auth.component.JwtComponent;
import com.project.course.backend.module.auth.dto.request.LoginRequest;
import com.project.course.backend.module.auth.dto.request.RegisterRequest;
import com.project.course.backend.module.auth.dto.response.LoginResponse;
import com.project.course.backend.module.auth.dto.response.RefreshTokenResponse;
import com.project.course.backend.module.auth.dto.response.RegisterResponse;
import com.project.course.backend.module.auth.entity.UserEntity;
import com.project.course.backend.module.auth.repository.AuthRepository;
import com.project.course.backend.module.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtComponent jwtComponent;

    @Mock
    private DtoEntityMapperTest dtoEntityMapperTest;

    @InjectMocks
    private AuthService authService;

    // input
    // expected output
    // actual output

    @BeforeEach
    void setUp() {
        // This method will run before each test, allowing you to set up common mock behavior if needed.
    }

    @Test
    void test_refreshToken_input_refreshTokenCorrect_ShouldReturn_RefreshTokenResponse() {
        String refreshToken = "correctRefreshToken";
        when(jwtComponent.validateToken(refreshToken)).thenReturn(true);

        // Mock the claims that would be returned by the JWT component
        Map<String, Object> claims = Map.of(
                AuthService.USER_ID, 1L,
                AuthService.EMAIL, "admin@gmail.com"
        );

        when(jwtComponent.extractClaims(refreshToken)).thenReturn(claims);
        when(jwtComponent.generateAccessToken(claims)).thenReturn("newAccessToken");
        when(jwtComponent.generateRefreshToken(claims)).thenReturn("newRefreshToken");

        // Act
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshToken, response);
        // Assert
        assertNotNull(refreshTokenResponse);
        assertEquals("newAccessToken", refreshTokenResponse.getNewAccessToken());
    }

    @Test
    void test_refreshToken_input_refreshTokenWrong_ShouldThrow_InvalidOrExpiredToken() {
        String refreshToken = "invalidRefreshToken";
        when(jwtComponent.validateToken(refreshToken)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.refreshToken(refreshToken, response));

        // Assert
        assertEquals("Invalid or expired refresh token", exception.getMessage());
    }

    @Test
    void test_refreshToken_input_Null_ShouldThrow_InvalidOrExpiredToken() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.refreshToken(null, response));

        // Assert
        assertEquals("Invalid or expired refresh token", exception.getMessage());
    }

    @Test
    void test_Register_ShouldBe_Success() {
        //input
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("admin");
        registerRequest.setPassword("1234");
        registerRequest.setUsername("admin");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");

        when(authRepository.findByEmail("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setUsername(registerRequest.getUsername());
        userEntity.setFirstName(registerRequest.getFirstName());
        userEntity.setLastName(registerRequest.getLastName());

        RegisterResponse mockRegisterResponse = new RegisterResponse();
        mockRegisterResponse.setEmail("admin");

        when(dtoEntityMapperTest.mapToEntity(registerRequest, UserEntity.class)).thenReturn(userEntity);
        when(authRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(dtoEntityMapperTest.mapToDto(userEntity, RegisterResponse.class)).thenReturn(mockRegisterResponse);

        RegisterResponse registerResponse = authService.register(registerRequest);
        // ตรวจสอบว่า registerResponse ไม่เป็น null
        assertNotNull(registerResponse);
        assertEquals("admin", registerResponse.getEmail());
    }

    @Test
    void test_Register_ShouldThrow_EmailAlreadyExist() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("admin");
        registerRequest.setPassword("1234");
        registerRequest.setUsername("admin");
        registerRequest.setFirstName("John");

        // mock ว่ามี user นี้อยู่แล้ว
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("admin");

        when(authRepository.findByEmail(registerRequest.getEmail())).
                thenReturn(Optional.of(existingUser));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                authService.register(registerRequest));

        // ตรวจสอบ message
        assertEquals("Email is already in use.", runtimeException.getMessage());
    }

    @Test
    void test_LoginUser_ShouldBe_InvalidEmailOrPassword() {
        String email = "test@example.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$someEncodedPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(authRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                authService.login(response, loginRequest));

        // ตรวจสอบ message
        verify(authRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
        verify(jwtComponent, never()).generateAccessToken(any());
        verify(jwtComponent, never()).setRefreshTokenCookie(any(), any());
        assertEquals("Invalid email or password", runtimeException.getMessage());
    }

    @Test
    void test_LoginUser_ShouldBe_EmailNotFound() {
        String email = "test@example.com";
        String rawPassword = "anyPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        when(authRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> authService.login(response, loginRequest)
        );

        // ตรวจสอบ message
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    void test_Login_Success_shouldReturnAccessToken() {

        String email = "test@example.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$someEncodedPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(authRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtComponent.generateAccessToken(anyMap())).thenReturn("accessToken");
        when(jwtComponent.generateRefreshToken(anyMap())).thenReturn("refreshToken");

        // Mock the setRefreshTokenCookie method to do nothing for void method
        // doNothing().when(jwtComponent).setRefreshTokenCookie(any(HttpServletResponse.class), eq("refreshToken"));

        LoginResponse loginResponse = authService.login(response, loginRequest);
        // ตรวจสอบว่า loginResponse ไม่เป็น null
        assertNotNull(loginResponse);
        assertEquals("accessToken", loginResponse.getAccessToken());

    }


}
