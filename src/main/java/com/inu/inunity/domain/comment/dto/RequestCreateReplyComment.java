package com.inu.inunity.domain.comment.dto;

public record RequestCreateReplyComment(
        Long commentId,
        Boolean isAnonymous,
        String content
) {
}
