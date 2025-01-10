package com.inu.inunity.domain.category;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.article.ArticleRepository;
import com.inu.inunity.domain.article.ArticleService;
import com.inu.inunity.domain.article.dto.ResponseArticleThumbnail;
import com.inu.inunity.domain.articleLike.ArticleLikeService;
import com.inu.inunity.domain.category.dto.RequestCreateCategory;
import com.inu.inunity.domain.category.dto.ResponseCategory;
import com.inu.inunity.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final ArticleLikeService articleLikeService;
    private final CommentService commentService;

    /**
     * 카테고리 생성 메서드
     *
     * @param requestCreateCategory Record Class
     * @return Long 생성된 Category ID
     * @author 김원정
     */
    @Transactional
    public Long createCategory(RequestCreateCategory requestCreateCategory) {
        Category newCategory = Category.builder()
                .name(requestCreateCategory.name())
                .description(requestCreateCategory.description())
                .icon(requestCreateCategory.icon())
                .isActive(true)
                .build();
        Category savedCategory = categoryRepository.save(newCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 목록 출력 메서드
     *
     * @return List<ResponseCategory> List 클래스에 감싸진 ResponseCategory
     * @author 김원정
     */
    @Transactional(readOnly = true)
    public List<ResponseCategory> findAllCategories() {
        List<Category> foundCategories = categoryRepository.findAll();
        return foundCategories.stream().map(category -> ResponseCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .isActive(category.getIsActive())
                .build()).toList();
    }

    /**
     * 카테고리 이름 수정 메서드
     *
     * @param categoryId    Category ID
     * @param category_name 바뀔 Category 이름
     * @return Long 수정된 Category ID
     * @author 김원정
     */
    @Transactional
    public Long updateCategory(Long categoryId, String category_name, String description, String icon, Boolean isActive, Boolean isNotice) {
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.CATEGORY_NOT_FOUND));
        foundCategory.updateCategory(category_name, description, icon, isActive, isNotice);
        Category savedCategory = categoryRepository.save(foundCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 활성 상태 변경 메서드
     *
     * @param category_id Category ID
     * @param status      바뀔 Category의 활성 상태
     * @return Long 수정된 Category ID
     * @author 김원정
     */
    @Transactional
    public Long changeStatus(Long category_id, boolean status) {
        Category foundCategory = categoryRepository.findById(category_id)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.CATEGORY_NOT_FOUND));
        foundCategory.changeStatus(status);
        Category savedCategory = categoryRepository.save(foundCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 삭제 메서드
     *
     * @param category_id Category ID
     * @author 김원정
     */
    @Transactional
    public void deleteCategory(Long category_id) {
        categoryRepository.deleteById(category_id);
    }


    /**
     * 카테고리가 가지고 있는 아티클을 모두 보여주는 메서드
     *
     * @param category_id 카테고리 ID
     * @return page 클래스에 감싸진 responseArticleForList 클래스
     * @author 김원정
     */
    @Transactional(readOnly = true)
    public Page<ResponseArticleThumbnail> getArticles(Long category_id, UserDetails userDetails, Pageable pageable) {
        Page<Article> pagingArticle = articleRepository.findAllByCategoryId(category_id, pageable);

        return pagingArticle.map(article -> {
            Boolean isLiked = articleLikeService.isLike(article.getId(), articleService.getUserIdAtUserDetails(userDetails));
            Integer likeNum = articleLikeService.getLikeNum(article);
            Integer commentNum = commentService.getCommentNum(article.getId());
            return ResponseArticleThumbnail.of(article, likeNum, isLiked, commentNum);
        });
    }
}
