package com.inu.inunity.domain.article.dto;

import com.inu.inunity.domain.article.Article;
import lombok.Builder;

import java.time.LocalDateTime;

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
        LocalDateTime updateAt,
        Integer viewNum,
        Integer commentNum,
        Integer likeNum,
        Boolean isLiked
) {

    public static ResponseArticleThumbnail of(Article article, Integer likeNum, Boolean isLiked, Integer commentNum){
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
                .updateAt(article.getUpdateAt())
                .viewNum(article.getView())
                .likeNum(likeNum)
                .isLiked(isLiked)
                .build();
    }
}
