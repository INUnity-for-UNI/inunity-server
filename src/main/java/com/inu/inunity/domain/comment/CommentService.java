package com.inu.inunity.domain.comment;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.article.ArticleRepository;
import com.inu.inunity.domain.articleUser.ArticleUserService;
import com.inu.inunity.domain.comment.dto.RequestCreateComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateComment;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import com.inu.inunity.domain.comment.dto.ResponseReplyComment;
import com.inu.inunity.domain.comment.replyComment.ReplyCommentService;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ReplyCommentService replyCommentService;
    private final ArticleUserService articleUserService;

    @Transactional(readOnly = true)
    public List<ResponseComment> getComments(Article article, Long userId){
        if(userId == null){
            return getCommentsForUnLoginUser(article);
        }
        else{
            return getCommentsForLoginUser(article, userId);
        }
    }

    public List<ResponseComment> getCommentsForLoginUser(Article article, Long userId) {
        return article.getComments().stream().map(comment -> {
            boolean commentIsOwner = Objects.equals(comment.getUser().getId(), userId);
            boolean commentIsArticleOwner = Objects.equals(comment.getUser().getId(), article.getUser().getId());
            String nickname;
            if(commentIsArticleOwner){
                nickname = comment.getIsAnonymous() ? "익명" + "(글쓴이)" : comment.getUser().getNickname();
            }
            else{
                nickname = comment.getIsAnonymous() ? "익명" + getUserAnonymousNum(comment.getUser().getId(), article.getId()) : comment.getUser().getNickname();
            }
            String profileImage = comment.getIsAnonymous() ? "https://image-server.squidjiny.com/pictures/다운로드 (1).jpeg" : comment.getUser().getProfileImageUrl();
            if (comment.getIsDeleted()) {
                return ResponseComment.ofDeleted(comment.getId(), replyCommentService.getReplyComment(comment, userId));
            } else {
            return ResponseComment.of(
                    comment,
                    nickname,
                    profileImage,
                    commentIsOwner,
                    replyCommentService.getReplyComment(comment, userId)
            );
        }
    }).toList();
    }

    public Integer getUserAnonymousNum(Long userId, Long articleId){
        return articleRepository.findAnonymousIdByArticleIdAndUserId(articleId, userId);
    }

    public List<ResponseComment> getCommentsForUnLoginUser(Article article) {
        return article.getComments().stream()
                .map(comment -> {
                    if(comment.getIsDeleted()){
                        return ResponseComment.ofDeleted(comment.getId(),
                                comment.getReplyComments().stream()
                                        .map(replyComment -> ResponseReplyComment.of(replyComment, false))
                                        .toList());
                    }
                    else{
                        boolean commentIsArticleOwner = Objects.equals(comment.getUser().getId(), article.getUser().getId());
                        String nickname;
                        if(commentIsArticleOwner){
                            nickname = comment.getIsAnonymous() ? "익명" + "(글쓴이)" : comment.getUser().getNickname();
                        }
                        else{
                            nickname = comment.getIsAnonymous() ? "익명" + getUserAnonymousNum(comment.getUser().getId(), article.getId()) : comment.getUser().getNickname();
                        }
                        String profileImage = comment.getIsAnonymous() ? "https://image-server.squidjiny.com/pictures/다운로드 (1).jpeg" : comment.getUser().getProfileImageUrl();
                        return ResponseComment.of(comment, nickname, profileImage, false,
                                comment.getReplyComments().stream()
                                        .map(replyComment -> ResponseReplyComment.of(replyComment, false))
                                        .toList());
                    }
                })
                .toList();
    }

    public Integer getCommentNum(Long articleId){
        return commentRepository.countByArticleId(articleId) + replyCommentService.getReplyCommentNum(articleId);
    }

    /**
     * 댓글을 작성하는 메서드
     * @author 김원정
     * @param requestCreateComment RequestCreateComment Record
     * @param articleId 연관관계 맺을 아티클 ID
     * @param userId 연관관계 맺을 유저 ID
     * @return Long 작성된 댓글의 ID
     */
    @Transactional
    public Long createComment(RequestCreateComment requestCreateComment, Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));
        Comment comment = Comment.of(requestCreateComment, user, article);
        articleUserService.sendNotification(article);
        articleUserService.createArticleUser(article, user);
        if(!articleRepository.existsByAnonymousUserIdAndArticleId(article.getId(), userId)){
            article.addAnonymousUser(userId);
        }
        commentRepository.save(comment);
        return articleId;
    }


    /**
     * 댓글을 수정하는 메서드
     * @author 김원정
     * @param requestUpdateComment RequestUpdateComment Record
     * @return Long 수정된 댓글의 ID
     */
    @Transactional
    public Long updateComment(RequestUpdateComment requestUpdateComment) {
        Comment comment = commentRepository.findById(requestUpdateComment.commentId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));
        comment.modifyComment(requestUpdateComment);
        return comment.getId();
    }

    /**
     * 댓글을 삭제하는 메서드
     * @author 김원정
     * @param commentId Comment ID
     */
    @Transactional
    public void deleteComment(Long commentId) {
       Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.COMMENT_NOT_FOUND));

        comment.deleteComment();
    }
}
