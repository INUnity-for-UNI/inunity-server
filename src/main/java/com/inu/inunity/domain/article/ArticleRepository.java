package com.inu.inunity.domain.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<Article> findAllByUserIdAndIsDeletedIsFalse(Long userId, Pageable pageable);
}
