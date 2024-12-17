package com.inu.inunity.security.oauth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.JwtProvider;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotFoundElementException;
import com.inu.inunity.security.exception.NotInformationMajorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

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
        String customAccessToken = request.getParameter("state");

        Long userId = jwtProvider.getMemberId(customAccessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        String oauth2UserName = oAuth2User.getName();
        certificationUpdate(user, oauth2UserName, authentication);
        redirectToken(request, response, user);
    }

    private void redirectToken(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Long memberId = user.getId();
        String accessToken = jwtProvider.createAccessToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String refreshToken = jwtProvider.createRefreshToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String uri = createURI(response, accessToken, refreshToken).toString();

        getRedirectStrategy().sendRedirect(request, response, uri);
    }


    private void certificationUpdate(User user, String oAuth2Name, Authentication authentication) {
        String[] parts = oAuth2Name.split("/");

        if (parts.length != 2) {
            throw new NotInformationMajorException(ExceptionMessage.EMAIL_NOT_UNDEFINED);
        }
        String name = parts[0];
        String department = parts[1];

        List<Role> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> Role.fromString(grantedAuthority.getAuthority()))
                .collect(Collectors.toList());

        user.updateAuthentication(name, department, roles);
    }

    private URI createURI(HttpServletResponse response, String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        jwtProvider.setTokenCookies(response, accessToken, refreshToken);
        return UriComponentsBuilder.newInstance()
                .scheme(frontScheme)
                .host(frontHost)
                .port(frontPort)
                .path(frontPath)
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}