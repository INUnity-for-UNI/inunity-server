package com.inu.inunity.domain.replyComment.dto;

public record RequestUpdateReplyComment(
        Long replyCommentId,
        Boolean isAnonymous,
        String content
) {
}
