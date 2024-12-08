package com.inu.inunity.security.oauth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
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
import java.util.Optional;
import java.util.StringTokenizer;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Attributes attributes = OAuth2Attributes.ofGoogle(provider, originAttributes);
        validateEmail(attributes.getEmail());
        validateDepartment(attributes.getName());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, attributes.getAttributes(), attributes.getNameAttributesKey());
    }

    public void validateEmail(String email){
        StringTokenizer st = new StringTokenizer(email, "@");
        String emailId = st.nextToken();
        String provider = st.nextToken();

        if(!provider.equals("inu.ac.kr")){
            throw new RuntimeException("학교이메일아님");
        }
    }

    public void validateDepartment(String oAuth2Name){
        StringTokenizer st = new StringTokenizer(oAuth2Name, "/");

        String name = st.nextToken();
        String department = st.nextToken();

        if(!(department.equals("컴퓨터공학부") || department.equals("정보통신공학과") || department.equals("임베디드시스템공학과"))){
            throw new RuntimeException("정보대 소속 학과가 아님");
        }
    }

}