package com.inu.inunity.domain.notice;

import com.inu.inunity.domain.notice.dto.NoticeDTO;
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
