package com.inu.inunity.domain.category.dto;
import lombok.Builder;

@Builder
public record ResponseCategory(
        Long id,
        String name,
        String icon,
        String description,
        Boolean isActive,
        Boolean isNotice
) {
}
