package com.inu.inunity.domain.comment;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.article.ArticleRepository;
import com.inu.inunity.domain.comment.dto.RequestCreateComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    /**
     * 댓글을 작성하는 메서드
     * @author 김원정
     * @param requestCreateComment RequestCreateComment Record
     * @param article_id 연관관계 맺을 아티클 ID
     * @param user_id 연관관계 맺을 유저 ID
     * @return Long 작성된 댓글의 ID
     */
    @Transactional
    public Long createComment(RequestCreateComment requestCreateComment, Long article_id, Long user_id) {
        Article foundArticle = articleRepository.findById(article_id)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        User foundUser = userRepository.findById(user_id)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));
        Comment newComment = Comment.builder()
                .content(requestCreateComment.content())
                .isAnonymous(requestCreateComment.isAnonymous())
                .user(foundUser)
                .article(foundArticle)
                .build();
        Comment savedComment = commentRepository.save(newComment);
        return savedComment.getId();
    }

    /**
     * 댓글을 수정하는 메서드
     * @author 김원정
     * @param requestUpdateComment RequestUpdateComment Record
     * @return Long 수정된 댓글의 ID
     */
    @Transactional
    public Long modifyComment(RequestUpdateComment requestUpdateComment) {
        Comment foundComment = commentRepository.findById(requestUpdateComment.commentId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));
        foundComment.modifyComment(requestUpdateComment);
        Comment savedComment = commentRepository.save(foundComment);
        return savedComment.getId();
    }

    /**
     * 댓글을 삭제하는 메서드
     * @author 김원정
     * @param comment_id Comment ID
     */
    @Transactional
    public void deleteComment(Long comment_id) {
        commentRepository.deleteById(comment_id);
    }
}
