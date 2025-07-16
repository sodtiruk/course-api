package com.project.course.backend.module.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegisterRequest {

    @Schema(example = "admin")
    private String username;

    @Schema(example = "admin@gmail.com")
    private String email;

    @Schema(example = "1234")
    private String password;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

}
