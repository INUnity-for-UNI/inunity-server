package com.inu.inunity.domain.advertise;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseAdvertise(
        Long advertiseId,
        String title,
        String content,
        String url,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {

    public static ResponseAdvertise of(Long advertiseId, String title, String content, String url, LocalDateTime createAt, LocalDateTime updateAt){
        return ResponseAdvertise.builder()
                .advertiseId(advertiseId)
                .title(title)
                .content(content)
                .url(url)
                .createAt(createAt)
                .updateAt(updateAt)
                .build();
    }
}
