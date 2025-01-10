package com.inu.inunity.domain.comment.replyComment;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.comment.dto.RequestCreateReplyComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateReplyComment;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Boolean isAnonymous;

    private Boolean isDeleted;

    private Boolean isInadequate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @Builder
    public ReplyComment(String content, Boolean isAnonymous, Boolean isDeleted, Boolean isInadequate, User user, Comment comment) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDeleted = isDeleted;
        this.isInadequate = isInadequate;
        this.user = user;
        this.comment = comment;
    }

    public static ReplyComment of(RequestCreateReplyComment requestCreateReplyComment, User user, Comment comment) {
        return ReplyComment.builder()
                .content(requestCreateReplyComment.content())
                .isAnonymous(requestCreateReplyComment.isAnonymous())
                .isDeleted(false)
                .isInadequate(false)
                .user(user)
                .comment(comment)
                .build();
    }

    public void updateReplyComment(RequestUpdateReplyComment requestUpdateReplyComment) {
        this.content = requestUpdateReplyComment.content();
        this.isAnonymous = requestUpdateReplyComment.isAnonymous();
    }

    public void deleteReplyComment() {
        this.isDeleted = true;
    }
}
