package com.inu.inunity.domain.notice.dto;

import lombok.Builder;

@Builder
public record NoticeDTO(
        Long id,
        String title,
        String departmentName,
        Long noticeDetailId
) {

}