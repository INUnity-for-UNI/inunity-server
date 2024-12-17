package com.inu.inunity.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.security.exception.NotInformationMajorException;
import com.inu.inunity.security.exception.NotSchoolEmailException;
import com.inu.inunity.security.exception.NullTokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        log.error("[OAuth2FailHandler] Exception: {}, Message: {}", exception.getClass().getSimpleName(), exception.getMessage());

        HttpStatus status;
        if (exception instanceof NotSchoolEmailException || exception instanceof NotInformationMajorException || exception instanceof NullTokenException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorResponse = new ObjectMapper().writeValueAsString(
                CommonResponse.error(status, exception.getMessage(), null));
        response.getWriter().write(errorResponse);
    }
}
