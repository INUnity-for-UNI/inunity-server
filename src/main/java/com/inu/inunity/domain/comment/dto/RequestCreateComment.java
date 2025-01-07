package com.inu.inunity.domain.comment.dto;

public record RequestCreateComment(
    Boolean isAnonymous,
    String content
) {
}
