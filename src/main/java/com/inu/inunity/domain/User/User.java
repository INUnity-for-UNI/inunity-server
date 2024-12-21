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
import com.inu.inunity.security.Role;
import com.inu.inunity.security.auth.LoginRegisterRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private Long studentId;

    private String nickname;

    private String description;

    private String department;

    private Boolean isGraduation;

    private LocalDate graduateDate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

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

    @Builder
    public User(Long studentId, String name , String nickname, String description, Boolean isGraduation, String department, List<Role> roles){
        this.name = name;
        this.studentId = studentId;
        this.nickname = nickname;
        this.description = description;
        this.isGraduation = isGraduation;
        this.department = department;
        this.roles = roles;
    }

    public User updateAuthentication(String department, List<Role> roles){
        this.roles = roles;
        this.department = department;
        return this;
    }

    public User setUser(String name, String nickname, LocalDate graduateDate){
        this.name = name;
        this.nickname = nickname;
        this.graduateDate = graduateDate;
        return this;
    }

    public User updateUser(String nickname, LocalDate graduateDate){
        this.nickname = nickname;
        this.graduateDate = graduateDate;
        return this;
    }

    public static User of(LoginRegisterRequest request, List<Role> roles){
        return User.builder()
                .studentId(request.getStudentId())
                .roles(roles)
                .build();
    }
}
