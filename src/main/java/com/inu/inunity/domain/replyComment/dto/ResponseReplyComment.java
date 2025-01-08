package com.inu.inunity.domain.replyComment.dto;

import com.inu.inunity.domain.replyComment.ReplyComment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseReplyComment(
        Long replyCommentId,
        Long userId,
        Boolean isAnonymous,
        String department,
        String nickname,
        String userImageUrl,
        String content,
        Boolean isOwner,
        LocalDateTime createAt
) {

    public static ResponseReplyComment of(ReplyComment replyComment, Boolean isOwner){
        return ResponseReplyComment.builder()
                .replyCommentId(replyComment.getId())
                .userId(replyComment.getUser().getId())
                .isAnonymous(replyComment.getIsAnonymous())
                .department(replyComment.getUser().getDepartment())
                .nickname(replyComment.getUser().getNickname())
                .userImageUrl(replyComment.getUser().getProfileImageUrl())
                .content(replyComment.getContent())
                .isOwner(isOwner)
                .createAt(replyComment.getCreateAt())
                .build();
    }

    public static ResponseReplyComment ofDeleted(Long replyCommentId) {
        return ResponseReplyComment.builder()
                .replyCommentId(replyCommentId)
                .isOwner(false)
                .content("[삭제된 댓글입니다.]")
                .build();
    }
}
