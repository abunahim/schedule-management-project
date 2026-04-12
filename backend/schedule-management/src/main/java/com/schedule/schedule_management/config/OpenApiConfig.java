package com.schedule.schedule_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scheduleOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Schedule Management API")
                        .description("REST API for managing schedules — built with Spring Boot, PostgreSQL, Redis, Docker and Kubernetes")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Nahim")
                                .url("https://github.com/abunahim/schedule-management-project")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}