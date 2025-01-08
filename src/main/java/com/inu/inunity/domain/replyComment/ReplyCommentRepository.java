package com.inu.inunity.domain.replyComment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
    Integer countByCommentArticleId(Long articleId);
}
