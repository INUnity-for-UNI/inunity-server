package com.inu.inunity.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notice_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "date", nullable = false, length = 20)
    private String date;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images;

    @Column(name = "attachments")
    private String attachments;

}
