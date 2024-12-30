package com.inu.inunity.domain.article;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.article.dto.ResponseArticle;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**
     * 아티클을 생성하는 메서드
     * @author 김원정
     * @param requestCreateArticle RequestCreateArticle Record
     * @param category_id 아티클이 소속될 Category ID
     * @param user_id 아티클을 생성하도록 요청한 User ID
     * @return Long 생성된 아티클의 게시글 번호
     */
    @Transactional
    Long createArticle(RequestCreateArticle requestCreateArticle, Long category_id, Long user_id) {
        Category foundCategory = categoryRepository.findById(category_id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        User foundUser = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article newArticle = Article.builder()
                .title(requestCreateArticle.title())
                .content(requestCreateArticle.content())
                .isAnonymous(requestCreateArticle.isAnonymous())
                .view(0)
                .isDeleted(false)
                .category(foundCategory)
                .user(foundUser)
                .build();

        Article savedArticle = articleRepository.save(newArticle);
        return savedArticle.getId();
    }

    /**
     * 아티클 단 건 조회 메서드
     * @author 김원정
     * @param article_id 아티클 ID
     * @return responseArticle Record
     */
    @Transactional(readOnly = true)
    ResponseArticle getArticle(Long article_id) {
        Article foundArticle = articleRepository.findById(article_id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        foundArticle.increaseView();
        Article savedArticle = articleRepository.save(foundArticle);
        return ResponseArticle.builder()
                .id(savedArticle.getId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .view(savedArticle.getView())
                .isDeleted(savedArticle.getIsDeleted())
                .isAnonymous(savedArticle.getIsAnonymous())
                .build();
    }

    /**
     * 아티클 업데이트 메서드
     * @author 김원정
     * @param article_id 수정될 아티클의 ID
     * @param requestModifyArticle 수정된 Record
     * @return Long 수정된 아티클의 게시글 번호
     */
    @Transactional
    Long modifyArticle(Long article_id, RequestModifyArticle requestModifyArticle) {
        Article foundArticle = articleRepository.findById(article_id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        foundArticle.modifyArticle(requestModifyArticle);
        Article savedArticle = articleRepository.save(foundArticle);
        return savedArticle.getId();
    }

    /**
     * 아티클 삭제 메서드
     * @author 김원정
     * @param article_id 삭제될 아티클의 ID
     */
    @Transactional
    void deleteArticle(Long article_id) {
        articleRepository.deleteById(article_id);
    }

}
