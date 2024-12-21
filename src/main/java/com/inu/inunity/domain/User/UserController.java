package com.inu.inunity.domain.User;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.User.dto.RequestSetUser;
import com.inu.inunity.domain.User.dto.RequestUpdateUser;
import com.inu.inunity.security.auth.LoginRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @PostMapping("/v1/users/{userid}")
    public ResponseEntity<CommonResponse> setUserInfo(@RequestBody RequestSetUser request, @AuthenticationPrincipal UserDetails userDetails) {
        userService.setUser(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("사용자에 대한 기본정보 기입 성공", null));
    }

    @PutMapping ("/v1/users/{userid}")
    public ResponseEntity<CommonResponse> updateUserInfo(@RequestBody RequestUpdateUser request, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUser(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("사용자 정보를 수정하는 기능 성공", null));
    }

}
