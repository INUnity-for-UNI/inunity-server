package com.inu.inunity.security.oauth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.JwtProvider;
import com.inu.inunity.security.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.StringTokenizer;

@Component
@RequiredArgsConstructor
@Slf4j
class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    @Value("${spring.front-redirect-url.host}")
    private String frontHost;

    @Value("${spring.front-redirect-url.port}")
    private int frontPort;

    @Value("${spring.front-redirect-url.scheme}")
    private String frontScheme;

    @Value("${spring.front-redirect-url.path}")
    private String frontPath;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Long userId = jwtProvider.getMemberId(jwtProvider.getAuthorizationToken(request));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("존재하는 유저가 아님"));

        String oauth2UserName = oAuth2User.getName();
        certificationUpdate(user, oauth2UserName);
        redirectToken(request, response, user);
    }

    private void redirectToken(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {

        Long memberId = user.getId();

        String accessToken = jwtProvider.createAccessToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String refreshToken = jwtProvider.createRefreshToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String uri = createURI(response, accessToken, refreshToken, memberId).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }


    private void certificationUpdate(User user, String oauth2UserName) {
        StringTokenizer st = new StringTokenizer(oauth2UserName, "/");
        String name = st.nextToken();
        String department = st.nextToken();

        user.updateAuthentication(name, department, List.of(Role.ROLE_USER));
    }

    private URI createURI(HttpServletResponse response, String accessToken, String refreshToken, Long userId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        setTokenCookies(response, accessToken, refreshToken);
        log.info("{}", frontHost);
        return UriComponentsBuilder.newInstance()
                .scheme(frontScheme)
                .host(frontHost)
                .port(frontPort)
                .path(frontPath)
                .queryParams(queryParams)
                .build()
                .toUri();
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