package com.inu.inunity.security.auth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.CustomUserDetails;
import com.inu.inunity.security.JwtProvider;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.exception.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    private static final String AUTH_SERVER_URL = "https://api.inuappcenter.kr/account/status";

    public Boolean loginWithAuthServer(LoginRegisterRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = request.getStudentId() + ":" + request.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(body.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        System.out.println(encodedAuth);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    AUTH_SERVER_URL,
                    HttpMethod.GET,
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
            throw new AlreadyRegisteredException(ExceptionMessage.USER_ALREADY_REGISTERED);
        }
    }

    public void login(HttpServletResponse response, LoginRegisterRequest request){
        User user = userRepository.findByStudentId(request.getStudentId())
                .orElseThrow(()->new NotRegisteredException(ExceptionMessage.USER_NOT_REGISTERED));
        if(!loginWithAuthServer(request)){
            throw new PortalLoginException(ExceptionMessage.PORTAL_LOGIN_FAILED_ID_PASSWORD_INCORRECT);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentId(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentId(), user.getRoles());

        setTokenCookies(response, accessToken, refreshToken);
    }

    @Transactional
    public void register(HttpServletResponse response, LoginRegisterRequest request){
        isRegister(request);

        if(!loginWithAuthServer(request)){
            throw new PortalLoginException(ExceptionMessage.PORTAL_LOGIN_FAILED_ID_PASSWORD_INCORRECT);
        }

        User user = User.of(request, List.of(Role.ROLE_TEST));

        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentId(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentId(), user.getRoles());

        setTokenCookies(response, accessToken, refreshToken);
    }

    @Transactional
    public String testCookie(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();

        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        return user.getName()+ " "+ user.getStudentId() + " " + user.getRoles() + " " + user.getDepartment();
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
