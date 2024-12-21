package com.inu.inunity.security.oauth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.Department;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotInformationMajorException;
import com.inu.inunity.security.exception.NotSchoolEmailException;
import com.inu.inunity.security.exception.NullTokenException;
import com.inu.inunity.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String customAccessToken = request.getParameter("state");

        //TODO: jwt 토큰 에러 관련 filter 추가하면서 이것도 같이 처리하기
        if (customAccessToken.equals("NO_STATE")) {
            throw new NullTokenException(new OAuth2Error("0", ExceptionMessage.TOKEN_NOT_FOUND.getMessage(), null));
        }

        String oAuth2UserName = oAuth2User.getAttribute("name");
        String oAuth2UserEmail = oAuth2User.getAttribute("email");
        validateEmail(oAuth2UserEmail);

        Long userId = jwtProvider.getMemberId(customAccessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error("NotFoundElementException", ExceptionMessage.USER_NOT_FOUND.getMessage(), null)));

        certificationUpdate(user, oAuth2UserName, authentication);
        redirectToken(request, response, user);
    }

    private void redirectToken(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Long memberId = user.getId();

        String accessToken = jwtProvider
                .createAccessToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String refreshToken = jwtProvider
                .createRefreshToken(memberId, user.getStudentId(), List.of(Role.ROLE_USER));
        String uri = createURI(response, accessToken, refreshToken).toString();

        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private void certificationUpdate(User user, String oAuth2Name, Authentication authentication) {
        String[] parts = oAuth2Name.split("/");
        validateMajor(parts);
        String name = parts[0];
        String department = parts[1];

        List<Role> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> Role.fromString(grantedAuthority.getAuthority()))
                .collect(Collectors.toList());

        user.updateAuthentication(name, department, roles);
        userRepository.save(user);
    }

    private void validateEmail(String email) {
        String[] parts = email.split("@");

        if (parts.length != 2) {
            log.info("[validateEmail] {}", ExceptionMessage.EMAIL_NOT_INU.getMessage());
            throw new NotSchoolEmailException(new OAuth2Error("0", ExceptionMessage.EMAIL_NOT_INU.getMessage(), null));
        }

        String provider = parts[1];
        if (!"inu.ac.kr".equals(provider)) {
            log.info("[validateEmail] {}", ExceptionMessage.EMAIL_NOT_INU.getMessage());
            throw new NotSchoolEmailException(new OAuth2Error("0", ExceptionMessage.EMAIL_NOT_INU.getMessage(), null));
        }
    }

    private void validateMajor(String[] parts) {
        if (parts.length != 2) {
            log.info("[validateMajor] {}", ExceptionMessage.EMAIL_NOT_UNDEFINED.getMessage());
            throw new NotInformationMajorException(new OAuth2Error("0", ExceptionMessage.EMAIL_NOT_UNDEFINED.getMessage(), null));
        }

        String department = parts[1];
        if (!Department.isValidDepartment(department)) {
            log.info("[validateMajor] {}", ExceptionMessage.EMAIL_NOT_UNDEFINED.getMessage());
            throw new NotInformationMajorException(new OAuth2Error("0", ExceptionMessage.EMAIL_NOT_UNDEFINED.getMessage(), null));
        }
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