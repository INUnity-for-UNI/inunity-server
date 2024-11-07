package com.inu.inunity.repository;

import com.inu.inunity.domain.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
}
