package com.inu.inunity.domain.articleUser;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleUserRepository extends JpaRepository<ArticleUser, Long> {
    Optional<ArticleUser> findByArticleAndUser(Article article, User user);
}
