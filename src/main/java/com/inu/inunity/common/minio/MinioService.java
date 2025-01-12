package com.inu.inunity.common.minio;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.ImageServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final S3Client s3Client;

    public String uploadFile(String bucketName, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new ImageServerException(ExceptionMessage.IMAGE_UPLOAD_FAILED);
        }
        File tempFile = File.createTempFile("minio-", fileName);
        file.transferTo(tempFile);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                tempFile.toPath()
        );

        tempFile.delete();
        return makeImagePath(bucketName, fileName);
    }

    public String makeImagePath(String bucketName, String fileName){
        return "/"+bucketName+"/"+fileName;
    }
}