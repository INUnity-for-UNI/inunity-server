package com.inu.inunity.domain.article.dto;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import com.inu.inunity.domain.notice.Notice;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record ResponseArticle(
        Long userId,
        String department,
        String nickname,
        String userImageUrl,
        Boolean isAnonymous,
        Long articleId,
        String title,
        String content,
        Integer viewNum,
        Boolean isOwner,
        Boolean isNotice,
        Integer likeNum,
        Boolean isLiked,
        String url,
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        Integer commentNum,
        List<ResponseComment> comments
) {

    public static ResponseArticle ofNormal(Article article, Integer likeNum, Boolean isLiked, Boolean isOwner, Integer commentNum,
                                           List<ResponseComment> comments) {
        String nickname = article.getIsAnonymous() ? "익명" : article.getUser().getNickname();
        String profileUrl = article.getUser().getIsAnonymous() ? "https://image-server.squidjiny.com/pictures/다운로드 (1).jpeg" : article.getUser().getProfileImageUrl();
        return ResponseArticle.builder()
                .userId(article.getUser().getId())
                .department(article.getUser().getDepartment())
                .nickname(nickname)
                .userImageUrl(profileUrl)
                .isAnonymous(article.getIsAnonymous())
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .viewNum(article.getView())
                .isOwner(isOwner)
                .likeNum(likeNum)
                .isLiked(isLiked)
                .isNotice(false)
                .createAt(article.getCreateAt())
                .updatedAt(article.getUpdateAt())
                .commentNum(commentNum)
                .comments(comments)
                .build();
    }

    public static ResponseArticle ofNotice(Article article, Notice notice, Integer likeNum, Boolean isLiked,Integer commentNum,
                                           List<ResponseComment> comments) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(notice.getDetail().getDate(), dateFormatter);
        return ResponseArticle.builder()
                .department(notice.getDepartment().getName())
                .nickname(notice.getDetail().getAuthor())
                .isAnonymous(article.getIsAnonymous())
                .articleId(article.getId())
                .title(notice.getTitle())
                .content(notice.getDetail().getContent())
                .viewNum(article.getView())
                .isOwner(false)
                .likeNum(likeNum)
                .isLiked(isLiked)
                .isNotice(true)
                .url(notice.getUrl())
                .createAt(localDate.atStartOfDay())
                .updatedAt(localDate.atStartOfDay())
                .commentNum(commentNum)
                .comments(comments)
                .build();
    }
}
