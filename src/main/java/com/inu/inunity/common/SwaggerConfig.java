package com.inu.inunity.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
@OpenAPIDefinition
        (
        info = @io.swagger.v3.oas.annotations.info.Info(title = "InUnity API", version = "1.0", description = "API Documentation"),
        servers = {@Server(url = "https://server.inunity.club", description = "최경민 개멍청이"),
                @Server(url = "https://inunity-server.squidjiny.com", description = "Production Server"),
                @Server(url = "http://localhost:8082", description = "local Server")
        }
)
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


    private Info info() {
        return new Info()
                .title("")
                .version("v0.1")
                .description("");
    }
}
