package com.inu.inunity.domain.replyComment;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.replyComment.dto.RequestCreateReplyComment;
import com.inu.inunity.domain.replyComment.dto.RequestUpdateReplyComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    public ReplyComment(String content, Boolean isAnonymous, Boolean isDeleted, User user, Comment comment) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDeleted = isDeleted;
        this.user = user;
        this.comment = comment;
    }

    public static ReplyComment of(RequestCreateReplyComment requestCreateReplyComment, User user, Comment comment) {
        return new ReplyComment(requestCreateReplyComment.content(), requestCreateReplyComment.isAnonymous(), false, user, comment);
    }

    public void updateReplyComment(RequestUpdateReplyComment requestUpdateReplyComment) {
        this.content = requestUpdateReplyComment.content();
        this.isAnonymous = requestUpdateReplyComment.isAnonymous();
    }

    public void deleteReplyComment() {
        this.isDeleted = true;
    }
}
