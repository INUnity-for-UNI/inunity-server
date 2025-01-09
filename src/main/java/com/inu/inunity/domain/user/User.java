package com.inu.inunity.domain.user;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleReport.ArticleReport;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.profile.career.Career;
import com.inu.inunity.domain.profile.contract.Contract;
import com.inu.inunity.domain.profile.portfolio.Portfolio;
import com.inu.inunity.domain.profile.skill.Skill;
import com.inu.inunity.domain.replyComment.ReplyComment;
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

    private String profileImageUrl;

    private Boolean isGraduation;

    private LocalDate graduateDate;

    private Boolean isAnonymous;

    private String organization;

    private String job;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<ArticleReport> articleReports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<ReplyComment> replyComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Portfolio> portfolios = new ArrayList<>();

    @Builder
    public User(Long studentId, String name , String nickname, String profileImageUrl, String description, Boolean isGraduation,
                String department, Boolean isAnonymous, String organization, String job, List<Role> roles){
        this.name = name;
        this.studentId = studentId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.isGraduation = isGraduation;
        this.department = department;
        this.isAnonymous = isAnonymous;
        this.organization = organization;
        this.job = job;
        this.roles = roles;
    }

    public User updateAuthentication(String department, List<Role> roles){
        this.roles = roles;
        this.department = department;
        return this;
    }

    public void setUser(String name, String nickname, LocalDate graduateDate, Boolean isGraduation){
        this.name = name;
        this.nickname = nickname;
        this.graduateDate = graduateDate;
        this.isGraduation = isGraduation;
        this.isAnonymous = true;
    }

    public void updateUserProfile(String nickname, LocalDate graduateDate, Boolean isGraduation,
                           Boolean isAnonymous, String organization, String job){
        this.nickname = nickname;
        this.graduateDate = graduateDate;
        this.isGraduation = isGraduation;
        this.isAnonymous = isAnonymous;
        this.organization = organization;
        this.job = job;
    }

    public static User of(LoginRegisterRequest request, List<Role> roles){
        return User.builder()
                .studentId(request.getStudentId())
                .profileImageUrl(null)
                .roles(roles)
                .build();
    }
}
