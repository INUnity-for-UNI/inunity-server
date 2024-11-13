package com.inu.inunity.dto.notice;

import lombok.Builder;

@Builder
public record NoticeDTO(
        Long id,
        String title,
        String departmentName,
        Long noticeDetailId
) {

}