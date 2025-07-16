package com.project.course.backend.module.auth.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.course.backend.common.dto.BaseErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.project.course.backend.common.constant.CommonConstant.ErrorMessage.UNAUTHORIZED;
import static com.project.course.backend.common.constant.CommonConstant.StatusCode.UNAUTHORIZED_CODE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException
    {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseErrorResponse body = BaseErrorResponse.builder()
                .message(UNAUTHORIZED)
                .code(UNAUTHORIZED_CODE)
                .build();

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
