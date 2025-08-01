package com.project.course.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info()
                        .title("Spring Boot REST API Swagger")
                        .description("Spring Boot REST API Swagger")
                        .contact(new Contact()
                                .name("Sutthirak Sutsaenya")
                                .email("sutthiraksutsaenya@gmail.com"))
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("authorization"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("authorization",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}