package com.inu.inunity.domain.article;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleReport.ArticleReport;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.User.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private Long view;

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
}
