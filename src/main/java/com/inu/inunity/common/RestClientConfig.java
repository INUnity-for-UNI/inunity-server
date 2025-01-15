package com.inu.inunity.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.inuappcenter.kr")
                .defaultHeader("User-Agent", "UNI PORTAL LOGIN")
                .build();
    }

    @Bean
    public RestClient restClientForAI() {
        return RestClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }
}