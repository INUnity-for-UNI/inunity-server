package com.inu.inunity.domain.replyComment;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.comment.CommentRepository;
import com.inu.inunity.domain.replyComment.dto.RequestCreateReplyComment;
import com.inu.inunity.domain.replyComment.dto.RequestUpdateReplyComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Integer getReplyCommentNum(Long articleId) {
        return replyCommentRepository.countByCommentArticleId(articleId);
    }

    @Transactional
    public Long createReplyComment(RequestCreateReplyComment requestCreateReplyComment, Long userId) {
        Comment comment = commentRepository.findById(requestCreateReplyComment.commentId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        ReplyComment replyComment = ReplyComment.of(requestCreateReplyComment, user, comment);
        replyCommentRepository.save(replyComment);

        return comment.getArticle().getId();
    }

    @Transactional
    public Long modifyReplyComment(RequestUpdateReplyComment requestUpdateReplyComment) {
        ReplyComment replyComment = replyCommentRepository.findById(requestUpdateReplyComment.replyCommentId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));

        replyComment.modifyReplyComment(requestUpdateReplyComment);

        return replyComment.getComment().getArticle().getId();
    }

    @Transactional
    public void deleteReplyComment(Long replyCommentId) {
        replyCommentRepository.deleteById(replyCommentId);
    }
}
