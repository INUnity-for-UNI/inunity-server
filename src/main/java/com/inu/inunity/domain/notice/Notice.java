package com.inu.inunity.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "notice_number", nullable = false)
    private Integer noticeNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false)
    private String url;

    // 관계 설정: NoticeDetail과 1:1 구성 허용
    @OneToOne(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private NoticeDetail detail;

}
