package com.inu.inunity.domain.profile;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/users/{userid}/profile/career")
    public ResponseEntity<CommonResponse<?>> createUserCareer(@RequestBody RequestCreateCareer request, @PathVariable Long userid,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        profileService.createCareer(request, userid, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("career 생성 성공", null));
    }

    @GetMapping("/users/{userid}/profile/career")
    public CommonResponse<?> getUserCareers(@PathVariable Long userid,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("유저의 career 가져오기 성공", profileService.getCareers(userDetails));
    }

    @PutMapping("/users/{userid}/profile/career")
    public CommonResponse<?> updateUserCareers(@RequestBody RequestUpdateCareer request, @PathVariable Long userid,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateCareer(request, userid, userDetails);
        return CommonResponse.success("career 수정 성공", null);
    }

    @DeleteMapping("/users/{userid}/profile/career/{careerid}")
    public CommonResponse<?> deleteUserCareers(@PathVariable Long careerid, @PathVariable Long userid,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteCareer(careerid, userid, userDetails);
        return CommonResponse.success("career 삭제 성공", null);
    }


}
