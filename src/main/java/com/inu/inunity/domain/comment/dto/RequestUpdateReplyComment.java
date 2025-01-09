package com.inu.inunity.domain.comment.dto;

public record RequestUpdateReplyComment(
        Long replyCommentId,
        Boolean isAnonymous,
        String content
) {
}
