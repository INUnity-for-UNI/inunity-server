package com.inu.inunity.common.fcm;

import com.inu.inunity.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FcmController {

    private final FcmTokenService fcmTokenService;

    public FcmController(FcmTokenService fcmTokenService) {
        this.fcmTokenService = fcmTokenService;
    }

    @PostMapping("/v1/fcm/token")
    public ResponseEntity<CommonResponse<?>> saveToken(@RequestParam String token, @AuthenticationPrincipal UserDetails userDetails) {
        fcmTokenService.saveFcmToken(token, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("유저에 토큰 추가 성공", null));
    }
}