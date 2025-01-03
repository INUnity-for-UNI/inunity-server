package com.inu.inunity.domain.article.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseArticle(
        Long userId,
    String department,
    String nickname,
    String userImageUrl,
    Long articleId,
    String title,
    String content,
    Boolean isAnonymous,
    Integer view,
    Boolean isDeleted,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
