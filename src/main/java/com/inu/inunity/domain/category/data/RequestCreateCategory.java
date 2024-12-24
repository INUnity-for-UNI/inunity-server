package com.inu.inunity.domain.category.data;

import lombok.Builder;

@Builder
public record RequestCreateCategory(
    String name,
    String description
) {
}
