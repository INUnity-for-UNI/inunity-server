package com.inu.inunity.domain.article.dto;

public record RequestModifyArticle(
        String title,
        String content,
        Boolean isAnonymous
) {
}
