package com.inu.inunity.domain.category;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.article.dto.ResponseArticleThumbnail;
import com.inu.inunity.domain.articleReport.SearchType;
import com.inu.inunity.domain.category.dto.RequestCreateCategory;
import com.inu.inunity.domain.category.dto.ResponseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 카테고리 생성 메서드
     * @author 김원정
     * @param requestCreateCategory @RequestBody Record Class
     * @return CommonResponse에 감싸진 Long Category ID
     */
    @PostMapping("/v1/categories")
    CommonResponse<Long> createCategory(@RequestBody RequestCreateCategory requestCreateCategory) {
        Long result = categoryService.createCategory(requestCreateCategory);
        return CommonResponse.success("카테고리 생성 완료", result);
    }

    /**
     * 카테고리 목록 출력 메서드
     * @author 김원정
     * @return CommonResponse에 감싸진 List 클래스에 감싸진 ResponseCategory
     */
    @GetMapping("/v1/categories")
    CommonResponse<List<ResponseCategory>> getAllCategories() {
        List<ResponseCategory> result = categoryService.findAllCategories();
        return CommonResponse.success("카테고리 목록 조회 완료", result);
    }

    /**
     * 카테고리 이름 수정 메서드
     * @author 김원정
     * @param category_id @RequestParam Category ID
     * @param category_name @RequestParam 바뀔 Category 이름
     * @return CommonResponse에 감싸진 Long Category ID
     */
    @PutMapping("/v1/categories/{category-id}")
    CommonResponse<Long> updateCategoryName(
            @PathVariable("category-id") Long category_id,
            @RequestParam("categoryName") String category_name,
            @RequestParam String icon,
            @RequestParam Boolean isActive,
            @RequestParam Boolean isNotice,
            @RequestParam String description
    ) {
        Long result = categoryService.updateCategory(category_id, category_name, description, icon, isActive, isNotice);
        return CommonResponse.success("카테고리 이름 수정 완료", result);
    }

    /**
     * 카테고리 활성 상태 변경 메서드
     * @author 김원정
     * @param category_id @RequestParam Category ID
     * @param status @RequestParam 바뀔 Category의 활성 상태
     * @return CommonResponse에 감싸진 수정된 Long Category ID
     */
    @PutMapping("/v1/categories/status")
    CommonResponse<Long> updateCategoryStatus(
            @PathVariable("category_id") Long category_id,
            @RequestParam Boolean status
    ) {
        Long result = categoryService.changeStatus(category_id, status);
        return CommonResponse.success("카테고리 활성 상태 수정 완료", result);
    }

    /**
     * 카테고리 삭제 메서드
     * @author 김원정
     * @param category_id @PathValue Category ID
     */
    @DeleteMapping("/v1/categories/{category_id}")
    CommonResponse<Void> deleteCategory(@PathVariable("category_id") Long category_id) {
        categoryService.deleteCategory(category_id);
        return CommonResponse.success("카테고리 삭제 완료", null);
    }


    /**
     * 카테고리가 가지고 있는 아티클을 모두 보여주는 메서드
     * @author 김원정
     * @param category_id @PathValue Category ID
     * @return CommonReponse에 감싸진 Page 클래스에 감싸진 ResponseArticleForList
     */
    @GetMapping("/v1/categories/{category_id}/articles")
    CommonResponse<Page<ResponseArticleThumbnail>> getArticlesByCategory(
            @PathVariable("category_id") Long category_id,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        Page<ResponseArticleThumbnail> result = categoryService.getArticles(category_id, userDetails, pageable);
        return CommonResponse.success("카테고리 내 아티클 목록 조회 완료", result);
    }

    @GetMapping("/v1/articles/search")
    CommonResponse<Page<ResponseArticleThumbnail>> searchArticles(
            @RequestParam(required = false) Long category_id,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "all") SearchType searchType,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        Page<ResponseArticleThumbnail> result = categoryService.getSearchArticles(category_id, keyword, searchType.name(), userDetails, pageable);
        return CommonResponse.success("카테고리 내 아티클 목록 검색 완료", result);
    }
}
