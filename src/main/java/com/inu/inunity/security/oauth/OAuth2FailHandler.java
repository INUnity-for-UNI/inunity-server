package com.inu.inunity.security.oauth;

import com.inu.inunity.common.exception.NotInformationMajorException;
import com.inu.inunity.common.exception.NotSchoolEmailException;
import com.inu.inunity.common.exception.NullTokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailHandler implements AuthenticationFailureHandler {

    @Value("${spring.front-redirect-url.host}")
    private String frontHost;

    @Value("${spring.front-redirect-url.port}")
    private int frontPort;

    @Value("${spring.front-redirect-url.scheme}")
    private String frontScheme;

    @Value("${spring.front-redirect-url.fail-path}")
    private String frontPath;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        log.error("[OAuth2FailHandler] Exception: {}, Message: {}", exception.getClass().getSimpleName(), exception.getMessage());

        HttpStatus status = getHttpStatus(exception);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        sendRedirect(request, response, status, exception);
    }

    private HttpStatus getHttpStatus(Exception exception){
        if (exception instanceof NotSchoolEmailException || exception instanceof NotInformationMajorException ||
                exception instanceof NullTokenException) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    private void sendRedirect(HttpServletRequest request, HttpServletResponse response,
                              HttpStatus status, Exception exception) throws IOException {
        String url = createURI(status, exception.getMessage()).toString();
        redirectStrategy.sendRedirect(request, response, url);
    }

    private URI createURI(HttpStatus status, String message) {
        int statusValue = status.value();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("code", Integer.toString(statusValue));
        queryParams.add("message", message);

        return UriComponentsBuilder.newInstance()
                .scheme(frontScheme)
                .host(frontHost)
                .port(frontPort)
                .path(frontPath)
                .queryParams(queryParams)
                .encode()
                .build()
                .toUri();
    }
}
