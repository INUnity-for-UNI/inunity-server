package com.inu.inunity.security.oauth;

import com.inu.inunity.security.Role;
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
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.ROLE_USER.getRole()));

        return new DefaultOAuth2User(authorities, attributes.getAttributes(), attributes.getNameAttributesKey());
    }
}