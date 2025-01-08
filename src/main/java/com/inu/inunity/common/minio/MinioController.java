package com.inu.inunity.common.minio;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.ImageServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping(value = "/v1/minio/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<?>> uploadFile(@RequestParam("bucket") String bucketName,
                                     @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommonResponse.success("이미지 업로드 성공", minioService.uploadFile(bucketName, file)));
        } catch (Exception e) {
            throw new ImageServerException(ExceptionMessage.IMAGE_UPLOAD_FAILED_UNDEFINED);
        }
    }
}
