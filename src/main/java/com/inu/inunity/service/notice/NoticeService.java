package com.inu.inunity.service.notice;

import com.inu.inunity.domain.notice.Notice;
import com.inu.inunity.domain.notice.NoticeRepository;
import com.inu.inunity.dto.notice.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 인천대학교 (전체) 공지사항 목록을 페이징 조회하는 메서드입니다.
     * @author 김원정
     */
    public Page<NoticeDTO> findAllByUniversity(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllByDepartmentName("인천대학교", pageable);
        return notices.map(this::convertToDTO);
    }

    private NoticeDTO convertToDTO(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .noticeDetailId(notice.getDetail().getId())
                .departmentName(notice.getDepartment().getName())
                .title(notice.getTitle())
                .build();
    }
}
