package com.inu.inunity.domain.article.dto;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.notice.Notice;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public record ResponseArticleThumbnail(
        Long userId,
        String userImageUrl,
        String nickname,
        String department,
        Boolean isAnonymous,
        Long articleId,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        Integer viewNum,
        Integer commentNum,
        Integer likeNum,
        Boolean isLiked,
        Boolean isNotice
) {

    public static ResponseArticleThumbnail ofNormal(Article article, Integer likeNum, Boolean isLiked, Integer commentNum){
        return ResponseArticleThumbnail.builder()
                .userId(article.getUser().getId())
                .department(article.getUser().getDepartment())
                .nickname(article.getUser().getNickname())
                .userImageUrl(article.getUser().getProfileImageUrl())
                .isAnonymous(article.getIsAnonymous())
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .commentNum(commentNum)
                .createAt(article.getCreateAt())
                .updatedAt(article.getUpdateAt())
                .viewNum(article.getView())
                .likeNum(likeNum)
                .isLiked(isLiked)
                .isNotice(false)
                .build();
    }

    public static ResponseArticleThumbnail ofNotice(Article article, Notice notice, Integer likeNum, Boolean isLiked, Integer commentNum){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(notice.getDetail().getDate(), dateFormatter);
        return ResponseArticleThumbnail.builder()
                .department(notice.getDepartment().getName())
                .nickname(notice.getDetail().getAuthor())
                .isAnonymous(article.getIsAnonymous())
                .articleId(article.getId())
                .title(notice.getTitle())
                .content(notice.getDetail().getContent())
                .commentNum(commentNum)
                .createAt(localDate.atStartOfDay())
                .updatedAt(localDate.atStartOfDay())
                .viewNum(article.getView())
                .likeNum(likeNum)
                .isLiked(isLiked)
                .isNotice(true)
                .build();
    }
}
