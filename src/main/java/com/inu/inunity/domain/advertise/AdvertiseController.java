package com.inu.inunity.domain.advertise;

import com.inu.inunity.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @PostMapping("/v1/advertises")
    public ResponseEntity<CommonResponse<Long>> createAdvertise(RequestCreateUpdateAdvertise requestCreateUpdateAdvertise) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("광고 생성 완료", advertiseService.createAdvertise(requestCreateUpdateAdvertise)));
    }

    @GetMapping("/v1/advertises")
    public CommonResponse<?> getAdvertises(Pageable pageable){
        return CommonResponse.success("광고들 가져오기 성공", advertiseService.getAdvertises(pageable));
    }

    @GetMapping("/v1/advertises/{advertiseid}")
    public CommonResponse<?> getAdvertises(@PathVariable Long advertiseid){
        return CommonResponse.success("광고 가져오기 성공", advertiseService.getAdvertise(advertiseid));
    }

    @PutMapping("/v1/advertises/{advertiseid}")
    public CommonResponse<?> editAdvertise(@PathVariable Long advertiseid, @RequestBody RequestCreateUpdateAdvertise requestCreateUpdateAdvertise){
        return CommonResponse.success("광고들 수정하기 성공", advertiseService.editAdvertise(advertiseid, requestCreateUpdateAdvertise));
    }

    @DeleteMapping("/v1/advertises/{advertiseid}")
    public CommonResponse<?> deleteAdvertise(@PathVariable Long advertiseid){
        advertiseService.deleteAdvertise(advertiseid);
        return CommonResponse.success("광고 삭제 성공", null);
    }
}
