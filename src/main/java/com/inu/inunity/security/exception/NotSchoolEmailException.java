package com.inu.inunity.security.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class NotSchoolEmailException extends OAuth2AuthenticationException {
    public NotSchoolEmailException(OAuth2Error oAuth2Error) {
        super(oAuth2Error);
    }
}
