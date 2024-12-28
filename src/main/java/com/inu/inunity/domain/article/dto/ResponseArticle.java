package com.inu.inunity.domain.article.dto;

import lombok.Builder;

@Builder
public record ResponseArticle(
    Long id,
    String title,
    String content,
    Boolean isAnonymous,
    Integer view,
    Boolean isDeleted
) {
}
