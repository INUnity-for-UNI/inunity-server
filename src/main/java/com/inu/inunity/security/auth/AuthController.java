package com.inu.inunity.security.auth;

import com.inu.inunity.common.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auth/login")
    public CommonResponse<?> login(@RequestBody LoginRegisterRequest request, HttpServletResponse response) {
        authService.login(response, request);
        return CommonResponse.success("로그인 성공", null);
    }

    @PostMapping("/v1/auth/register/login")
    public ResponseEntity<CommonResponse> register(@RequestBody LoginRegisterRequest request, HttpServletResponse response) {
        authService.register(response, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("회원가입 여부 체그용 로그인 성공", null));
    }

    @GetMapping("/v1/auth/test")
    public ResponseEntity<CommonResponse> register(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("회원가입 여부 체그용 로그인 성공", authService.testCookie(userDetails)));
    }

}
