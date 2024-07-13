package com.dnghkm.high_school_community.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "highschool community api",
                description = "highschool community api 명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
}