package com.inu.inunity.domain.article;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleReport.ArticleReport;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.notice.Notice;
import com.inu.inunity.domain.user.User;
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

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer view;

    private Boolean isDeleted;

    private Boolean isAnonymous;

    private Boolean isInadequate;

    private Boolean isNotice;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Notice notice;

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
    public Article(String title, String content, Boolean isAnonymous, Integer view, Boolean isDeleted, Boolean isInadequate,
                   Boolean isNotice, Notice notice, Category category,
                   User user) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.view = view;
        this.isDeleted = isDeleted;
        this.isInadequate = isInadequate;
        this.isNotice = isNotice;
        this.notice = notice;
        this.category = category;
        this.user = user;
    }

    public static Article ofUser(RequestCreateArticle request, Integer view, Boolean isDeleted, Category category,
                                 User user) {
        return Article.builder()
                .title(request.title())
                .content(request.content())
                .isAnonymous(request.isAnonymous())
                .view(view)
                .isDeleted(isDeleted)
                //todo: fastApi 연결 성공하면 상태변경시키기
                .isInadequate(false)
                .isNotice(false)
                .category(category)
                .user(user)
                .build();
    }

    public static Article ofNotice(Boolean isDeleted, Notice notice, Category category) {
        return Article.builder()
                .view(0)
                .isDeleted(isDeleted)
                .isInadequate(false)
                .isNotice(true)
                .notice(notice)
                .category(category)
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
