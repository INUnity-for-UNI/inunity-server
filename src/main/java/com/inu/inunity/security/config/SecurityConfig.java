package com.inu.inunity.security.config;

import com.inu.inunity.common.exception.JwtExceptionHandlerFilter;
import com.inu.inunity.security.jwt.JwtAuthFilter;
import com.inu.inunity.security.jwt.JwtProvider;
import com.inu.inunity.security.oauth.CustomAuthorizationRequestResolver;
import com.inu.inunity.security.oauth.CustomOAuth2Service;
import com.inu.inunity.security.oauth.OAuth2FailHandler;
import com.inu.inunity.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomOAuth2Service customOAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oauth2FailHandler;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .cors(security -> security.configurationSource(corsConfigurationSource()))
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .requestMatchers(HttpMethod.GET, PERMIT_GET).permitAll()
                        .requestMatchers(PERMIT_TEST_USER).hasAnyRole("TEST", "USER")
                        .requestMatchers(HttpMethod.GET, PERMIT_USER_GET).hasRole("USER")
                        .requestMatchers(HttpMethod.POST, PERMIT_USER_POST).hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, PERMIT_USER_PUT).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, PERMIT_USER_DELETE).hasRole("USER")
                        .anyRequest().hasRole("ADMIN")
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                        )
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2Service)
                        )
                        .failureHandler(oauth2FailHandler)
                        .successHandler(oAuth2SuccessHandler))
                .addFilterBefore(new JwtExceptionHandlerFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        return httpSecurity.build();
    }

    //TODO: 실 서비스 할 때 제한 걸어야됨 ㅋㅋㅋㅋㅋㅋ
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // 모든 출처 허용
        configuration.setAllowedMethods(List.of("*"));       // 모든 HTTP 메서드 허용 (GET, POST, PUT 등)
        configuration.setAllowedHeaders(List.of("*"));       // 모든 헤더 허용
        configuration.setAllowCredentials(true);             // 쿠키, 인증 정보 허용 (필요 시)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 설정 적용
        return source;
    }

    String[] PERMIT_ALL = {
            "/oauth2/**", //oauth2 로그인 서비스 접근
            "/login/**", //oauth2 로그인창
            "/swagger-ui/**", //스웨거 명세
            "/v3/api-docs/**", //스웨거 명세
            "/v1/auth/register",
            "/v1/auth/login",
            "/v1/users/admin"
    };

    String[] PERMIT_GET = {
            "/v1/categories",
            "/v1/popular/articles",
            "/v1/categories/{category_id}/articles",
            "/v1/articles/search",
            "/v1/articles/{article_id}",
            "/v1/users/{userid}/profile",
            "/v1/users/{userid}/profile/skill",
            "/v1/users/{userid}/profile/portfolio",
            "/v1/users/{userid}/profile/career",
            "/v1/advertises",
            "/v1/advertises/{advertiseid}"
    };

    String[] PERMIT_TEST_USER = {
            "/v1/user",
            "/v1/auth/test",
            "/v1/minio/upload"
    };

    String[] PERMIT_USER_GET = {
            "/v1/users/information",
            "/v1/users/comments",
            "/v1/users/articles/wrote",
            "/v1/users/articles/like",
            "/v1/notification",
            "/v1/notification/categories"
    };

    String[] PERMIT_USER_POST = {
            "/v1/replycomment",
            "/v1/articles/{articleid}/comment",
            "/v1/users/{userid}/profile/skill",
            "/v1/users/{userid}/profile/portfolio",
            "/v1/users/{userid}/profile/career",
            "/v1/articles/{category_id}",
            "/v1/articles/{article_id}/like",
            "/v1/fcm/token",
            "/v1/notification/categories/{categoryid}"
    };

    String[] PERMIT_USER_PUT = {
            "/v1/replycomment",
            "/v1/articles/comment",
            "/v1/articles/{articleid}",
            "/v1/users/{userid}/profile",
            "/v1/users/{userid}/profile/skill",
            "/v1/users/{userid}/profile/portfolio",
            "/v1/users/{userid}/profile/career",
            "/v1/notification/{notificationid}"
    };

    String[] PERMIT_USER_PATCH = {
    };

    String[] PERMIT_USER_DELETE = {
            "/v1/replyment/{replymentid}",
            "/v1/comment/{commentid}",
            "/v1/users/{userid}/profile/skill/{skillId}",
            "/v1/users/{userid}/profile/portfolio/{portfolioId}",
            "/v1/users/{userid}/profile/career/{careerId}",
            "/v1/articles/{article_id}"
    };

    String[] PERMIT_ADMIN = {
            "/v1/categories/{category-id}",
            "/v1/categories/status",
            "/v1/categories/{category_id}"
    };
}