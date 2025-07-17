package com.project.course.backend.module.auth.service;

import com.project.course.backend.common.component.DtoEntityMapper;
import com.project.course.backend.common.component.DtoEntityMapperTest;
import com.project.course.backend.module.auth.component.JwtComponent;
import com.project.course.backend.module.auth.dto.request.LoginRequest;
import com.project.course.backend.module.auth.dto.request.RegisterRequest;
import com.project.course.backend.module.auth.dto.response.LoginResponse;
import com.project.course.backend.module.auth.dto.response.RefreshTokenResponse;
import com.project.course.backend.module.auth.dto.response.RegisterResponse;
import com.project.course.backend.module.auth.entity.UserEntity;
import com.project.course.backend.module.auth.repository.AuthRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtComponent jwtComponent;
    private final DtoEntityMapperTest dtoEntityMapperTest;

    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";

    public LoginResponse login(HttpServletResponse response, LoginRequest loginRequest) {
        UserEntity userEntity = authRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_ID, userEntity.getUserId());
            claims.put(EMAIL, loginRequest.getEmail());
            String accessToken = jwtComponent.generateAccessToken(claims);
            String refreshToken = jwtComponent.generateRefreshToken(claims);

            // Set the refresh token in a secure cookie
            jwtComponent.setRefreshTokenCookie(response, refreshToken);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid email or password");
        }

    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        // check email exists already
        if (authRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        UserEntity userEntity = dtoEntityMapperTest.mapToEntity(registerRequest, UserEntity.class);

        //encoding password
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        userEntity.setPassword(encodedPassword);

        return dtoEntityMapperTest.mapToDto(
                authRepository.save(userEntity), RegisterResponse.class);
    }

    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || !jwtComponent.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        Map<String, Object> claims = jwtComponent.extractClaims(refreshToken);
        Long userId = Long.valueOf(claims.get(USER_ID).toString());
        String email = claims.get(EMAIL).toString();

        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put(USER_ID, userId);
        newClaims.put(EMAIL, email);

        String newAccessToken = jwtComponent.generateAccessToken(newClaims);
        String newRefreshToken = jwtComponent.generateRefreshToken(newClaims);

        // Set the refresh token in a secure cookie
        jwtComponent.setRefreshTokenCookie(response, newRefreshToken);

        return RefreshTokenResponse.builder()
                .newAccessToken(newAccessToken)
                .build();
    }
}
