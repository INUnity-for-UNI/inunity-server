package com.inu.inunity.domain.replyComment.dto;

public record RequestCreateReplyComment(
        Long commentId,
        Boolean isAnonymous,
        String content
) {
}
