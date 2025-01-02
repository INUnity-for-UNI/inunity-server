package com.inu.inunity.domain.user;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.user.dto.RequestSetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @PutMapping ("/v1/users")
    public ResponseEntity<CommonResponse> updateUserInfo(@RequestBody RequestSetUser request, @AuthenticationPrincipal UserDetails userDetails) {
        userService.setUser(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("사용자 정보를 수정하는 기능 성공", null));
    }
}
