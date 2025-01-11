package com.inu.inunity.domain.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByCategoryIdAndIsDeletedIsFalse(Long categoryId, Pageable pageable);
    Page<Article> findAllByUserIdAndIsDeletedIsFalse(Long userId, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND (a.content LIKE %:keyword% OR a.title LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryAndKeywordForContentOrTitle(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND (a.content LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryAndKeywordForContent(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND (a.title LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryAndKeywordForTitle(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.isNotice = false AND (a.content LIKE %:keyword% OR a.title LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryIsNoticeIsFalseAndKeywordForContentOrTitle(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.isNotice = false AND (a.content LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryIsNoticeIsFalseAndKeywordForContent(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT a FROM Article a WHERE a.category.isNotice = false AND (a.title LIKE %:keyword%) AND a.isDeleted = false")
    Page<Article> searchArticlesCategoryIsNoticeIsFalseAndKeywordForTitle(@Param("keyword") String keyword, Pageable pageable);

}
