package com.inu.inunity.domain.articleLike;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Integer countByArticleId(Long articleId);
    Optional<ArticleLike> findByArticleAndUser(Article article, User user);
    boolean existsArticleLikeByArticleIdAndUserId(Long articleId, Long userId);
    Page<ArticleLike> findAllByUserIdAndArticleIsDeletedIsFalse(Long userId, Pageable pageable);
}
