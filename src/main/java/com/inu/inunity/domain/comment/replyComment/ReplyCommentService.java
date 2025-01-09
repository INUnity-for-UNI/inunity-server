package com.inu.inunity.domain.comment.replyComment;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.comment.Comment;
import com.inu.inunity.domain.comment.CommentRepository;
import com.inu.inunity.domain.comment.dto.RequestCreateReplyComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateReplyComment;
import com.inu.inunity.domain.comment.dto.ResponseReplyComment;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<ResponseReplyComment> getReplyComment(Comment comment, Long userId){
        return comment.getReplyComments().stream().map(replyComment -> {
            boolean replyCommentIsOwner = Objects.equals(replyComment.getUser().getId(), userId);

            if (replyComment.getIsDeleted()) {
                return ResponseReplyComment.ofDeleted(replyComment.getId());
            } else {
                return ResponseReplyComment.of(replyComment, replyCommentIsOwner);
            }
        }).toList();
    }

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
    public Long updateReplyComment(RequestUpdateReplyComment requestUpdateReplyComment) {
        ReplyComment replyComment = replyCommentRepository.findById(requestUpdateReplyComment.replyCommentId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));

        replyComment.updateReplyComment(requestUpdateReplyComment);

        return replyComment.getComment().getArticle().getId();
    }

    @Transactional
    public void deleteReplyComment(Long replyCommentId) {
        ReplyComment replyComment = replyCommentRepository.findById(replyCommentId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));

        replyComment.deleteReplyComment();
    }
}
