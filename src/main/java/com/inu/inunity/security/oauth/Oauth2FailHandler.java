package com.inu.inunity.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2FailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        log.error("OAuth2 authentication failed: {}", exception.getMessage());

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauth2Exception = (OAuth2AuthenticationException) exception;
            String errorCode = oauth2Exception.getError().getErrorCode();
            String description = oauth2Exception.getError().getDescription();

            response.setHeader("X-OAuth2-Error", errorCode);
            response.setHeader("X-OAuth2-Error-Description", description);
        } else {
            response.setHeader("X-OAuth2-Error", "unknown_error");
            response.setHeader("X-OAuth2-Error-Description", exception.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.sendRedirect("/error-page");
    }
}
