package com.inu.inunity.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT-Cookie");

        SecurityScheme cookieAuth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("jwtToken");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("JWT-Cookie", cookieAuth))
                .addSecurityItem(securityRequirement)
                .info(info());
    }


    private io.swagger.v3.oas.models.info.Info info() {
        return new Info()
                .title("")
                .version("v0.1")
                .description("");
    }
}
