package com.inu.inunity.domain.category;

import com.inu.inunity.domain.category.data.RequestCreateCategory;
import com.inu.inunity.domain.category.data.ResponseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성 메서드
     * @author 김원정
     * @param requestCreateCategory Record Class
     * @return Long 생성된 Category ID
     */
    public Long createCategory(RequestCreateCategory requestCreateCategory) {
        Category newCategory = Category.builder()
                .name(requestCreateCategory.name())
                .description(requestCreateCategory.description())
                .isActive(true)
                .build();
        Category savedCategory = categoryRepository.save(newCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 목록 출력 메서드
     * @author 김원정
     * @return List<ResponseCategory> List 클래스에 감싸진 ResponseCategory
     */
    public List<ResponseCategory> findAllCategories() {
        List<Category> foundCategories = categoryRepository.findAll();
        return foundCategories.stream().map(category -> ResponseCategory.builder()
                 .id(category.getId())
                 .name(category.getName())
                 .description(category.getDescription())
                 .isActive(category.getIsActive())
                 .build()).toList();
    }

    /**
     * 카테고리 이름 수정 메서드
     * @author 김원정
     * @param category_id Category ID
     * @param category_name 바뀔 Category 이름
     * @return Long 수정된 Category ID
     */
    public Long editCategoryName(Long category_id, String category_name) {
        Category foundCategory = categoryRepository.findById(category_id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        foundCategory.editName(category_name);
        Category savedCategory = categoryRepository.save(foundCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 활성 상태 변경 메서드
     * @author 김원정
     * @param category_id Category ID
     * @param status 바뀔 Category의 활성 상태
     * @return Long 수정된 Category ID
     */
    public Long changeStatus(Long category_id, boolean status) {
        Category foundCategory = categoryRepository.findById(category_id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        foundCategory.changeStatus(status);
        Category savedCategory = categoryRepository.save(foundCategory);
        return savedCategory.getId();
    }

    /**
     * 카테고리 삭제 메서드
     * @author 김원정
     * @param category_id Category ID
     */
    public void deleteCategory(Long category_id) {
        categoryRepository.deleteById(category_id);
    }
}
