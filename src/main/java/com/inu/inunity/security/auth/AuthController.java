package com.inu.inunity.security.auth;

import com.inu.inunity.common.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auth/login")
    public CommonResponse<?> login(@RequestBody LoginRegisterRequest request, HttpServletResponse response) {
        authService.login(response, request);
        return CommonResponse.success("로그인 성공", null);
    }

    @PostMapping("/v1/auth/register")
    public ResponseEntity<CommonResponse> register(@RequestBody LoginRegisterRequest request, HttpServletResponse response) {
        authService.register(response, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("회원가입 성공", null));
    }
}
