package com.inu.inunity.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inu.inunity.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MemberEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[ClientEntryPointException]인증 실패");

        CommonResponse commonResponse = new CommonResponse(0, ex.getMessage(), null);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));

    }
}