package com.inu.inunity.domain.comment.dto;

public record RequestModifyComment(
        Long commentId,
        Boolean isAnonymous,
        String content
) {
}
