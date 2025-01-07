package com.inu.inunity.domain.comment.dto;

public record RequestUpdateComment(
        Long commentId,
        Boolean isAnonymous,
        String content
) {
}
