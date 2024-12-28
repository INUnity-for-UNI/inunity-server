package com.inu.inunity.domain.article.dto;

public record RequestCreateArticle(
        String title,
        String content,
        Boolean isAnonymous
) {
}
