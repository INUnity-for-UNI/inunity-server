package com.inu.inunity.domain.category.dto;

import lombok.Builder;

@Builder
public record RequestCreateCategory(
    String name,
    String description,
    String icon,
    Boolean isActivity,
    Boolean isNotice
) {
}
