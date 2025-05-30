package com.inu.inunity.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countByArticleId(Long articleId);
}
