package com.inu.inunity.domain.User;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleReport.ArticleReport;
import com.inu.inunity.domain.profile.career.Career;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.profile.contract.Contract;
import com.inu.inunity.domain.profile.portfolio.Portfolio;
import com.inu.inunity.domain.ReplyComment.ReplyComment;
import com.inu.inunity.domain.profile.skill.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long studentNumber;

    private String nickname;

    private String description;

    private Boolean isGraduation;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ArticleReport> articleReports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReplyComment> replyComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Portfolio> portfolios = new ArrayList<>();
}
