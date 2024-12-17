package com.inu.inunity.security.oauth;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotFoundElementException;
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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Attributes attributes = OAuth2Attributes.ofGoogle("sub", originAttributes);
        System.out.println(originAttributes.toString());
        validateEmail(attributes.getEmail());
        validateMajor(attributes.getName());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, attributes.getAttributes(), attributes.getNameAttributesKey());
    }

    public void validateEmail(String email){
        StringTokenizer st = new StringTokenizer(email, "@");
        String emailId = st.nextToken();
        String provider = st.nextToken();

        if(!provider.equals("inu.ac.kr")){
            throw new NotSchoolEmailException(ExceptionMessage.EMAIL_NOT_INU);
        }
    }

    public void validateMajor(String oAuth2Name){
        StringTokenizer st = new StringTokenizer(oAuth2Name, "/");
        String department;

        try{
            String name = st.nextToken();
            department = st.nextToken();
        }catch (NoSuchElementException e){
            throw new NotFoundElementException(ExceptionMessage.EMAIL_NOT_UNDEFINED);
        }

        if(!(department.equals("컴퓨터공학부") || department.equals("정보통신공학과") || department.equals("임베디드시스템공학과"))){
            throw new NotInformationMajorException(ExceptionMessage.USER_NOT_INFORMATION_MAJOR);
        }
    }

}