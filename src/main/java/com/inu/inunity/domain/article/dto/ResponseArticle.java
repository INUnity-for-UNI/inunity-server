package com.inu.inunity.domain.article.dto;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import com.inu.inunity.domain.notice.Notice;
import lombok.Builder;

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
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        Integer commentNum,
        List<ResponseComment> comments
) {

    public static ResponseArticle ofNormal(Article article, Integer likeNum, Boolean isLiked, Boolean isOwner, Integer commentNum,
                                           List<ResponseComment> comments) {
        return ResponseArticle.builder()
                .userId(article.getUser().getId())
                .department(article.getUser().getDepartment())
                .nickname(article.getUser().getNickname())
                .userImageUrl(article.getUser().getProfileImageUrl())
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

    public static ResponseArticle ofNotice(Article article, Notice notice, Integer likeNum, Boolean isLiked, Integer commentNum,
                                           List<ResponseComment> comments) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
                .createAt(LocalDateTime.parse(notice.getDetail().getDate(), formatter))
                .updatedAt(LocalDateTime.parse(notice.getDetail().getDate(), formatter))
                .commentNum(commentNum)
                .comments(comments)
                .build();
    }
}
