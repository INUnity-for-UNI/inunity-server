package com.inu.inunity.domain.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseMyPageComment(
        Long articleId,
        String articleTitle,
        Long commentId,
        String content,
        LocalDateTime createAt
) {

    public static ResponseMyPageComment of(Long articleId, String articleTitle, Long commentId, String content, LocalDateTime createAt){
        return ResponseMyPageComment.builder()
                .articleId(articleId)
                .articleTitle(articleTitle)
                .commentId(commentId)
                .content(content)
                .createAt(createAt)
                .build();
    }
}
