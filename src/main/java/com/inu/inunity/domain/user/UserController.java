package com.inu.inunity.domain.user;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.user.dto.RequestSetUser;
import com.inu.inunity.security.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @PutMapping ("/v1/users")
    @Operation(summary = "유저 초기정보 설정", description = "회원가입에서 쓰입니다.")
    public ResponseEntity<CommonResponse<?>> updateUserInfo(@RequestBody RequestSetUser request, @AuthenticationPrincipal UserDetails userDetails) {
        userService.setUser(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("사용자 정보를 수정하는 기능 성공", null));
    }

    @PutMapping ("/v1/users/admin")
    @Operation(summary = "나의 권한을 원하는대로 뾰로롱", description = "나의 권한을 불가촉 천민부터 이세계 최강자까지 자유롭게 변환 가능합니다")
    public ResponseEntity<CommonResponse> updateUserAdmin(@RequestParam Role role, @AuthenticationPrincipal UserDetails userDetails) {
        userService.editRole(role, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("권한 변경", null));
    }

    @GetMapping("/v1/users/articles/like")
    @Operation(summary = "유저가 조아요~한 글 수집", description = "유저가 좋아요 누른 글 리스트로...")
    public CommonResponse<?> getUserLikedArticle(@AuthenticationPrincipal UserDetails userDetails,
                                                 Pageable pageable) {
        return CommonResponse.success("유저가 좋아요누른 아티클 조회 성공", userService.getUserLikedArticle(userDetails, pageable));
    }

    @GetMapping("/v1/users/articles/wrote")
    @Operation(summary = "유저가 쓴 글 불러오기", description = "유저가 쓴 글을 불러옵니다")
    public CommonResponse<?> getUserWroteArticle(@AuthenticationPrincipal UserDetails userDetails,
                                                 Pageable pageable) {
        return CommonResponse.success("유저가 작성한 아티클 조회 성공", userService.getUserWroteArticle(userDetails, pageable));
    }

    @GetMapping("/v1/users/comments")
    @Operation(summary = "유저가 쓴 댓글 목록", description = "유저가 쓴 댓글을 불러옵니다.")
    public CommonResponse<?> getUserComments(@AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("유저가 단 댓글목록 조회 성공", userService.getUserComments(userDetails));
    }

    @GetMapping("/v1/users/information")
    public CommonResponse<?> getUserId(@AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("유저의 아이디 조회 성공", userService.getUserAtUserDetails(userDetails));
    }
}
