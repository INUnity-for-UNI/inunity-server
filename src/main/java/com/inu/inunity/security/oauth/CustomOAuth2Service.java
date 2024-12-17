package com.inu.inunity.security.oauth;

import com.inu.inunity.security.Department;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotInformationMajorException;
import com.inu.inunity.security.exception.NotSchoolEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);
        Map<String, Object> originAttributes = oAuth2User.getAttributes();
        OAuth2Attributes attributes = OAuth2Attributes.ofGoogle("sub", originAttributes);

        validateEmail(attributes.getEmail());
        validateMajor(attributes.getName());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.ROLE_USER.getRole()));

        return new DefaultOAuth2User(authorities, attributes.getAttributes(), attributes.getNameAttributesKey());
    }

    public void validateEmail(String email) {
        String[] parts = email.split("@");

        if (parts.length != 2) {
            throw new NotSchoolEmailException(ExceptionMessage.EMAIL_NOT_INU);
        }
        String provider = parts[1];

        if (!"inu.ac.kr".equals(provider)) {
            throw new NotSchoolEmailException(ExceptionMessage.EMAIL_NOT_INU);
        }
    }

    public void validateMajor(String oAuth2Name) {
        String[] parts = oAuth2Name.split("/");

        if (parts.length != 2) {
            throw new NotInformationMajorException(ExceptionMessage.EMAIL_NOT_UNDEFINED);
        }
        String department = parts[1];

        if (!Department.isValidDepartment(department)) {
            throw new NotInformationMajorException(ExceptionMessage.USER_NOT_INFORMATION_MAJOR);
        }
    }
}