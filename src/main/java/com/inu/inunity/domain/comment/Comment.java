package com.inu.inunity.domain.comment;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.comment.dto.RequestUpdateComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Boolean isAnonymous;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    @Builder
    Comment(String content, Boolean isAnonymous, User user, Article article) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.user = user;
        this.article = article;
    }

    void modifyComment(RequestUpdateComment requestUpdateComment) {
        this.content = requestUpdateComment.content();
        this.isAnonymous = requestUpdateComment.isAnonymous();
    }
}
