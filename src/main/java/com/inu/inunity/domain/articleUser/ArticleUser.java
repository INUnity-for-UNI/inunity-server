package com.inu.inunity.domain.articleUser;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Builder
    public ArticleUser(Long id, User user, Article article){
        this.id = id;
        this.user = user;
        this.article = article;
    }

    public static ArticleUser of(User user, Article article){
        return ArticleUser.builder()
                .user(user)
                .article(article)
                .build();
    }
}
