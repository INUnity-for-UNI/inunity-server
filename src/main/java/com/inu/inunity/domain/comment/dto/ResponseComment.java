package com.inu.inunity.domain.comment.dto;

import com.inu.inunity.domain.comment.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResponseComment(
        Long commentId,
        Long userId,
        Boolean isAnonymous,
        String department,
        String nickname,
        String userImageUrl,
        String content,
        Boolean isOwner,
        LocalDateTime createAt,
        List<ResponseReplyComment> replyComments
) {
    public static ResponseComment of(Comment comment, String nickname, String profileImage, Boolean isOwner, List<ResponseReplyComment> replyComments){
        return ResponseComment.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .isAnonymous(comment.getIsAnonymous())
                .department(comment.getUser().getDepartment())
                .nickname(nickname)
                .userImageUrl(profileImage)
                .content(comment.getContent())
                .isOwner(isOwner)
                .createAt(comment.getCreateAt())
                .replyComments(replyComments)
                .build();
    }

    public static ResponseComment ofDeleted(Long commentId, List<ResponseReplyComment> replyComments) {
        return ResponseComment.builder()
                .commentId(commentId)
                .nickname("(알수없음)")
                .isOwner(false)
                .userImageUrl("https://image-server.squidjiny.com/pictures/다운로드 (1).jpeg")
                .content("[삭제된 댓글입니다.]")
                .replyComments(replyComments)
                .build();
    }
}
