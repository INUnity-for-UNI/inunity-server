package com.inu.inunity.domain.comment;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.comment.dto.RequestCreateComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateComment;
import com.inu.inunity.domain.comment.replyComment.ReplyComment;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Boolean isAnonymous;

    private Boolean isDeleted;

    private Boolean isInadequate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<ReplyComment> replyComments = new ArrayList<>();

    @Builder
    Comment(String content, Boolean isAnonymous, Boolean isDeleted, Boolean isInadequate, User user, Article article) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDeleted = isDeleted;
        this.isInadequate = isInadequate;
        this.user = user;
        this.article = article;
    }

    public static Comment of(RequestCreateComment request, User user, Article article){
        return Comment.builder()
                .content(request.content())
                .isAnonymous(request.isAnonymous())
                .isInadequate(false)
                .user(user)
                .article(article)
                .build();
    }

    public void modifyComment(RequestUpdateComment requestUpdateComment) {
        this.content = requestUpdateComment.content();
        this.isAnonymous = requestUpdateComment.isAnonymous();
    }

    public void deleteComment(){
        this.isDeleted = true;
    }
}
