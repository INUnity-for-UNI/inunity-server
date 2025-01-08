package com.inu.inunity.domain.articleLike;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Integer countByArticleId(Long articleId);
    Optional<ArticleLike> findByArticleAndUser(Article article, User user);
    boolean existsArticleLikeByArticleIdAndUserId(Long articleId, Long userId);
}
