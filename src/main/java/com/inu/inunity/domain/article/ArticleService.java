package com.inu.inunity.domain.article;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.article.dto.ResponseArticle;
import com.inu.inunity.domain.article.dto.ResponseArticleThumbnail;
import com.inu.inunity.domain.articleLike.ArticleLike;
import com.inu.inunity.domain.articleLike.ArticleLikeService;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.category.CategoryRepository;
import com.inu.inunity.domain.comment.CommentService;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import com.inu.inunity.domain.comment.dto.ResponseMyPageComment;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserRepository;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ArticleLikeService articleLikeService;
    private final CommentService commentService;

    /**
     * 아티클을 생성하는 메서드
     * @author 김원정
     * @param requestCreateArticle RequestCreateArticle Record
     * @param categoryId 아티클이 소속될 Category ID
     * @param userId 아티클을 생성하도록 요청한 User ID
     * @return Long 생성된 아티클의 게시글 번호
     */
    @Transactional
    public Long createArticle(RequestCreateArticle requestCreateArticle, Long categoryId, Long userId) {
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.CONTRACT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));
        Article article = Article.ofUser(requestCreateArticle, 0, false, foundCategory, user);

        articleRepository.save(article);
        return article.getId();
    }

    /**
     * 아티클 단 건 조회 메서드
     * @author 김원정
     * @param articleId 아티클 ID
     * @return responseArticle Record
     */
    @Transactional(readOnly = true)
    public ResponseArticle getArticle(Long articleId, UserDetails userDetails) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));

        if(article.getIsDeleted()){
            throw new NotFoundElementException(ExceptionMessage.ARTICLE_IS_DELETED);
        }

        article.increaseView();
        Long userId = getUserIdAtUserDetails(userDetails);
        Integer likeNum = articleLikeService.getLikeNum(article);
        Boolean isLike = articleLikeService.isLike(articleId, userId);
        Integer commentNum = commentService.getCommentNum(articleId);
        List<ResponseComment> comments = commentService.getComments(article, userId);

        if(article.getCategory().getIsNotice()) {
            return ResponseArticle.ofNotice(article, article.getNotice(), likeNum, isLike, commentNum, comments);
        }
        else {
            Boolean isOwner = Objects.equals(article.getUser().getId(), userId);
            return ResponseArticle.ofNormal(article, likeNum, isLike, isOwner, commentNum, comments);
        }
    }

    /**
     * 아티클 업데이트 메서드
     * @author 김원정
     * @param articleId 수정될 아티클의 ID
     * @param requestModifyArticle 수정된 Record
     * @return Long 수정된 아티클의 게시글 번호
     */
    @Transactional
    public Long modifyArticle(Long articleId, RequestModifyArticle requestModifyArticle) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        article.modifyArticle(requestModifyArticle);
        return articleId;
    }

    /**
     * 아티클 삭제 메서드
     * @author 김원정
     * @param articleId 삭제될 아티클의 ID
     */
    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        article.deleteArticle();
    }

    public Long getUserIdAtUserDetails(UserDetails userDetails){
        if(userDetails == null){
            return null;
        }else{
            return ((CustomUserDetails) userDetails).getId();
        }
    }

    public Integer pushArticleLike(Long articleId, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        return articleLikeService.toggleLike(article, user);
    }

    @Transactional(readOnly = true)
    public Page<ResponseArticleThumbnail> getUserLikedArticles(Long userId, Pageable pageable){
        Page<ArticleLike> articleLikes = articleLikeService.getUserLikePost(userId, pageable);

        return articleLikes.map(articleLike -> {
                    Article article = articleLike.getArticle();
                    return ResponseArticleThumbnail.ofNormal(article, articleLikeService.getLikeNum(article),
                            articleLikeService.isLike(article.getId(), userId), commentService.getCommentNum(article.getId()));
                });
    }

    @Transactional(readOnly = true)
    public Page<ResponseArticleThumbnail> getUserWroteArticles(Long userId, Pageable pageable){
        Page<Article> articles = articleRepository.findAllByUserIdAndIsDeletedIsFalse(userId, pageable);

        return articles.map(article -> ResponseArticleThumbnail.ofNormal(article, articleLikeService.getLikeNum(article),
                                articleLikeService.isLike(article.getId(), userId), commentService.getCommentNum(article.getId())));
    }

    @Transactional(readOnly = true)
    public List<ResponseMyPageComment> getUserWroteComments(User user){
        return Stream.concat(
                        user.getComments().stream()
                                .filter(comment -> !comment.getIsDeleted())
                                .map(comment -> {
                                    Article article = comment.getArticle();
                                    return ResponseMyPageComment.of(
                                            article.getId(),
                                            article.getTitle(),
                                            comment.getId(),
                                            comment.getContent(),
                                            comment.getCreateAt()
                                    );
                                }),
                        user.getReplyComments().stream()
                                .filter(replyComment -> !replyComment.getIsDeleted())
                                .map(replyComment -> {
                                    Article article = replyComment.getComment().getArticle();
                                    return ResponseMyPageComment.of(
                                            article.getId(),
                                            article.getTitle(),
                                            replyComment.getId(),
                                            replyComment.getContent(),
                                            replyComment.getCreateAt()
                                    );
                                })
                ).sorted(Comparator.comparing(ResponseMyPageComment::createAt).reversed())
                .toList();
    }
}
