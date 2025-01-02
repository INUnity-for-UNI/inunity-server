package com.inu.inunity.domain.category.dto;

import lombok.Builder;

@Builder
public record ResponseArticleForList(
    Long id,
    String title,
    Boolean isAnonymous,
    Integer view,
    Boolean isDeleted
) {
}
