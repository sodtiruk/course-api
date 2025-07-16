package com.project.course.backend.module.auth.controller;

import com.project.course.backend.common.dto.BaseResponse;
import com.project.course.backend.module.auth.dto.request.LoginRequest;
import com.project.course.backend.module.auth.dto.request.RegisterRequest;
import com.project.course.backend.module.auth.dto.response.LoginResponse;
import com.project.course.backend.module.auth.dto.response.RefreshTokenResponse;
import com.project.course.backend.module.auth.dto.response.RegisterResponse;
import com.project.course.backend.module.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.course.backend.common.constant.CommonConstant.ErrorMessage.*;
import static com.project.course.backend.common.constant.CommonConstant.StatusCode.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            HttpServletResponse response,
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(BaseResponse.<LoginResponse>builder()
                .data(authService.login(response, loginRequest))
                .message(SUCCESS)
                .statusCode(SUCCESS_CODE)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(BaseResponse.<RegisterResponse>builder()
                .data(authService.register(registerRequest))
                .message(SUCCESS)
                .statusCode(SUCCESS_CODE)
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<RefreshTokenResponse>> refreshToken(
            @CookieValue(value = "x-refresh-token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(BaseResponse.<RefreshTokenResponse>builder()
                .data(authService.refreshToken(refreshToken, response))
                .message(SUCCESS)
                .statusCode(SUCCESS_CODE)
                .build());
    }

}
