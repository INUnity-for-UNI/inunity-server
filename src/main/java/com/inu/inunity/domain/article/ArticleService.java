package com.inu.inunity.domain.article;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.User.UserService;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.article.dto.ResponseArticle;
import com.inu.inunity.domain.articleLike.ArticleLikeService;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.category.CategoryRepository;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ArticleLikeService articleLikeService;
    private final UserService userService;

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
        Article article = Article.of(requestCreateArticle, 0, false, foundCategory, user);

        articleRepository.save(article);
        return article.getId();
    }

    /**
     * 아티클 단 건 조회 메서드
     * @author 김원정
     * @param article_id 아티클 ID
     * @return responseArticle Record
     */
    @Transactional(readOnly = true)
    public ResponseArticle getArticle(Long article_id, UserDetails userDetails) {
        Article article = articleRepository.findById(article_id)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));
        article.increaseView();

        //todo: 새 이슈에서 해당 기능 구현. 프론트와의 빠른 협업을 위해 mock 삽입 후 배포.
        Integer likeNum = articleLikeService.getLikeNum(article);
        Boolean isLike = articleLikeService.isLike(article_id, getUserIdAtUserDetails(userDetails));
        Integer commentNum = 0;
        Boolean isOwner = false;
        List<ResponseComment> comments = new ArrayList<>();

        return ResponseArticle.of(article, likeNum, isLike, isOwner, commentNum, comments);
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
        articleRepository.deleteById(articleId);
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

}
