package com.inu.inunity.security.auth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.exception.*;
import com.inu.inunity.security.jwt.CustomUserDetails;
import com.inu.inunity.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<User> isRegistered(LoginRegisterRequest request) {
        Optional<User> user = userRepository.findByStudentId(request.getStudentId());
        if (user.isPresent()) {
            if (user.get().getName().isEmpty()) {
                throw new UserRegisteredException(ExceptionMessage.USER_ALREADY_REGISTERED);
            }
        }

        return user;
    }

    public void login(HttpServletResponse response, LoginRegisterRequest request) {
        User user = userRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new NotRegisteredException(ExceptionMessage.USER_NOT_REGISTERED));
        if (!loginWithAuthServer(request)) {
            throw new PortalLoginException(ExceptionMessage.PORTAL_LOGIN_FAILED_ID_PASSWORD_INCORRECT);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentId(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentId(), user.getRoles());

        jwtProvider.setTokenCookies(response, accessToken, refreshToken);
    }

    @Transactional
    public void register(HttpServletResponse response, LoginRegisterRequest request) {
        if (!loginWithAuthServer(request)) {
            throw new PortalLoginException(ExceptionMessage.PORTAL_LOGIN_FAILED_ID_PASSWORD_INCORRECT);
        }
        User user = makeUser(isRegistered(request), request);
        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getStudentId(), user.getRoles());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getStudentId(), user.getRoles());

        jwtProvider.setTokenCookies(response, accessToken, refreshToken);
    }

    public User makeUser(Optional<User> user, LoginRegisterRequest request) {
        return user.orElseGet(() -> User.of(request, List.of(Role.ROLE_TEST)));
    }

    @Transactional
    public String testCookie(UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        return user.getName() + " " + user.getStudentId() + " " + user.getRoles() + " " + user.getDepartment();
    }
}
