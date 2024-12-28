package com.inu.inunity.domain.category;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.category.dto.RequestCreateCategory;
import com.inu.inunity.domain.category.dto.ResponseArticleForList;
import com.inu.inunity.domain.category.dto.ResponseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 카테고리 생성 메서드
     * @author 김원정
     * @param requestCreateCategory @RequestBody Record Class
     * @return CommonResponse에 감싸진 Long Category ID
     */
    @PostMapping
    CommonResponse<Long> createCategory(@RequestBody RequestCreateCategory requestCreateCategory) {
        Long result = categoryService.createCategory(requestCreateCategory);
        return CommonResponse.success("카테고리 생성 완료", result);
    }

    /**
     * 카테고리 목록 출력 메서드
     * @author 김원정
     * @return CommonResponse에 감싸진 List 클래스에 감싸진 ResponseCategory
     */
    @GetMapping
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
    @PutMapping("/name")
    CommonResponse<Long> updateCategoryName(
            @RequestParam("categoryId") Long category_id,
            @RequestParam("categoryName") String category_name
    ) {
        Long result = categoryService.editCategoryName(category_id, category_name);
        return CommonResponse.success("카테고리 이름 수정 완료", result);
    }

    /**
     * 카테고리 활성 상태 변경 메서드
     * @author 김원정
     * @param category_id @RequestParam Category ID
     * @param status @RequestParam 바뀔 Category의 활성 상태
     * @return CommonResponse에 감싸진 수정된 Long Category ID
     */
    @PutMapping("/status")
    CommonResponse<Long> updateCategoryStatus(
            @RequestParam("categoryId") Long category_id,
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
    @DeleteMapping("/{category_id}")
    CommonResponse<Void> deleteCategory(@PathVariable Long category_id) {
        categoryService.deleteCategory(category_id);
        return CommonResponse.success("카테고리 삭제 완료", null);
    }


    /**
     * 카테고리가 가지고 있는 아티클을 모두 보여주는 메서드
     * @author 김원정
     * @param category_id @PathValue Category ID
     * @return CommonReponse에 감싸진 Page 클래스에 감싸진 ResponseArticleForList
     */
    @GetMapping("/{category_id}/articles")
    CommonResponse<Page<ResponseArticleForList>> getArticlesByCategory(
            @PathVariable("category_id") Long category_id,
            Pageable pageable
    ) {
        Page<ResponseArticleForList> result = categoryService.getArticles(category_id, pageable);
        return CommonResponse.success("카테고리 내 아티클 목록 조회 완료", result);
    }
}
