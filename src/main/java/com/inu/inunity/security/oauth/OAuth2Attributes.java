package com.inu.inunity.security.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributesKey;
    private final String name;
    private final String email;
    private final String profileImageUrl;
    private final String provider;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributesKey,
                            String name, String email, String profileImageUrl, String provider) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .nameAttributesKey(userNameAttributeName)
                .provider("google")
                .build();
    }
}