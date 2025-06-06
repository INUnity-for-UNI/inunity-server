package com.inu.inunity.domain.profile;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareer;
import com.inu.inunity.domain.profile.portfolio.dto.RequestCreatePortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.RequestUpdatePortfolio;
import com.inu.inunity.domain.profile.skill.dto.RequestCreateSkill;
import com.inu.inunity.domain.profile.skill.dto.RequestUpdateSkill;
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
    @PostMapping("/v1/users/{userid}/profile/career")
    public ResponseEntity<CommonResponse<?>> createUserCareer(@RequestBody RequestCreateCareer request, @PathVariable Long userid,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        profileService.createCareer(request, userid, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("career 생성 성공", null));
    }

    @GetMapping("/v1/users/{userid}/profile/career")
    public CommonResponse<?> getUserCareers(@PathVariable Long userid) {
        return CommonResponse.success("유저의 career 가져오기 성공", profileService.getCareers(userid));
    }

    @PutMapping("/v1/users/{userid}/profile/career")
    public CommonResponse<?> updateUserCareer(@RequestBody RequestUpdateCareer request, @PathVariable Long userid,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateCareer(request, userid, userDetails);
        return CommonResponse.success("career 수정 성공", null);
    }

    @DeleteMapping("/v1/users/{userid}/profile/career/{careerid}")
    public CommonResponse<?> deleteUserCareer(@PathVariable Long careerid, @PathVariable Long userid,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteCareer(careerid, userid, userDetails);
        return CommonResponse.success("career 삭제 성공", null);
    }

    @GetMapping("/v1/users/{userid}/profile")
    public CommonResponse<?> getUserContracts(@PathVariable Long userid, @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("유저의 profile 가져오기 성공", profileService.getProfile(userid, userDetails));
    }

    @PutMapping("/v1/users/{userid}/profile")
    public CommonResponse<?> updateUserContract(@RequestBody RequestUpdateProfile request, @PathVariable Long userid,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateProfile(request, userDetails);
        return CommonResponse.success("profile 수정 성공", null);
    }

    @PostMapping("/v1/users/{userid}/profile/portfolio")
    public ResponseEntity<CommonResponse<?>> createUserPortfolio(@RequestBody RequestCreatePortfolio request, @PathVariable Long userid,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        profileService.createPortfolio(request, userid, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("portfolio 생성 성공", null));
    }

    @GetMapping("/v1/users/{userid}/profile/portfolio")
    public CommonResponse<?> getUserPortfolios(@PathVariable Long userid) {
        return CommonResponse.success("유저의 portfolio 가져오기 성공", profileService.getPortfolios(userid));
    }

    @PutMapping("/v1/users/{userid}/profile/portfolio")
    public CommonResponse<?> updateUserPortfolio(@RequestBody RequestUpdatePortfolio request, @PathVariable Long userid,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updatePortfolio(request, userid, userDetails);
        return CommonResponse.success("portfolio 수정 성공", null);
    }

    @DeleteMapping("/v1/users/{userid}/profile/portfolio/{portfolioid}")
    public CommonResponse<?> deleteUserPortfolio(@PathVariable Long portfolioid, @PathVariable Long userid,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        profileService.deletePortfolio(portfolioid, userid, userDetails);
        return CommonResponse.success("portfolio 삭제 성공", null);
    }

    @PostMapping("/v1/users/{userid}/profile/skill")
    public ResponseEntity<CommonResponse<?>> createUserSkill(@RequestBody RequestCreateSkill request, @PathVariable Long userid,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        profileService.createSkill(request, userid, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("skill 생성 성공", null));
    }

    @GetMapping("/v1/users/{userid}/profile/skill")
    public CommonResponse<?> getUserSkills(@PathVariable Long userid) {
        return CommonResponse.success("유저의 skill 가져오기 성공", profileService.getSkills(userid));
    }

    @PutMapping("/v1/users/{userid}/profile/skill")
    public CommonResponse<?> updateUserSkill(@RequestBody RequestUpdateSkill request, @PathVariable Long userid,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateSkill(request, userid, userDetails);
        return CommonResponse.success("skill 수정 성공", null);
    }

    @DeleteMapping("/v1/users/{userid}/profile/skill/{skillid}")
    public CommonResponse<?> deleteUserSkill(@PathVariable Long skillid, @PathVariable Long userid,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteSkill(skillid, userid, userDetails);
        return CommonResponse.success("skill 삭제 성공", null);
    }
}
