package com.inu.inunity.security.auth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.JwtProvider;
import com.inu.inunity.security.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    private static final String AUTH_SERVER_URL = "https://api.inuappcenter.kr/auth/login";

    public Boolean loginWithAuthServer(LoginRegisterRequest request) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문 설정
        String body = "id=" + request.studentId + "&password=" + request.password;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        try {
            // 인증 서버로 요청 전송
            ResponseEntity<Map> response = restTemplate.exchange(
                    AUTH_SERVER_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void isRegister(LoginRegisterRequest request) {
        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        }
    }

    public void login(HttpServletResponse response, LoginRegisterRequest request){
        User user = userRepository.findByStudentId(request.getStudentId())
                .orElseThrow(()->new RuntimeException("가입되지 않음."));
        if(!loginWithAuthServer(request)){
            throw new RuntimeException("포탈에 없는 아이디/비밀번호");
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentNumber(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentNumber(), user.getRoles());

        setTokenCookies(response, accessToken, refreshToken);
    }

    @Transactional
    public void register(HttpServletResponse response, LoginRegisterRequest request){
        isRegister(request);
        if(!loginWithAuthServer(request)){
            throw new RuntimeException("포탈에 없는 아이디/비밀번호");
        }

        User user = User.of(request, List.of(Role.ROLE_TEST));

        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentNumber(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentNumber(), user.getRoles());

        setTokenCookies(response, accessToken, refreshToken);
    }

    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access Token 쿠키
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();

        // Refresh Token 쿠키
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .build();

        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
