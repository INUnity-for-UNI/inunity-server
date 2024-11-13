package com.inu.inunity.controller;

import com.inu.inunity.dto.notice.NoticeDTO;
import com.inu.inunity.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/v1/university")
    public Page<NoticeDTO> getAllUniversity(
            @PageableDefault(size = 10, sort = "noticeNumber", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return noticeService.findAllByUniversity(pageable);
    }
}
