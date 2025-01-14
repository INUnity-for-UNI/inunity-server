package com.inu.inunity.domain.userCategory.dto;

import com.inu.inunity.domain.category.Category;
import lombok.Builder;

@Builder
public record ResponseUserCategory(
        Long categoryId,
        String categoryName,
        boolean isAlarm
) {

    public static ResponseUserCategory of(Category category, boolean isAlarm){
        return ResponseUserCategory.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .isAlarm(isAlarm)
                .build();
    }
}
