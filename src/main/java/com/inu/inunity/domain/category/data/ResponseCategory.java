package com.inu.inunity.domain.category.data;
import lombok.Builder;

@Builder
public record ResponseCategory(
        Long id,
        String name,
        String description,
        Boolean isActive
) {
}
