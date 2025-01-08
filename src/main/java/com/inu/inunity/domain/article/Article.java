package com.inu.inunity.domain.article;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleReport.ArticleReport;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Integer view;

    private Boolean isDeleted;

    private Boolean isAnonymous;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> articleLikes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleReport> articleReports;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder
    public Article(String title, String content, Boolean isAnonymous, Integer view, Boolean isDeleted, Category category,
                   User user) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.view = view;
        this.isDeleted = isDeleted;
        this.category = category;
        this.user = user;
    }

    public static Article of(RequestCreateArticle request, Integer view, Boolean isDeleted, Category category,
                   User user) {
        return Article.builder()
                .title(request.title())
                .content(request.content())
                .isAnonymous(request.isAnonymous())
                .view(view)
                .isDeleted(isDeleted)
                .category(category)
                .user(user)
                .build();
    }

    public void increaseView() {
        this.view++;
    }

    public void modifyArticle(RequestModifyArticle requestModifyArticle) {
        this.title = requestModifyArticle.title();
        this.content = requestModifyArticle.content();
        this.isAnonymous = requestModifyArticle.isAnonymous();
    }

    public void deleteArticle(){
        this.isDeleted = true;
    }
}
