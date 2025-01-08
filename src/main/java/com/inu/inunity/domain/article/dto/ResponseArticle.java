package com.inu.inunity.domain.article.dto;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.comment.dto.ResponseComment;
import lombok.Builder;

import java.time.LocalDateTime;
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
        Integer likeNum,
        Boolean isLiked,
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        Integer commentNum,
        List<ResponseComment> comments
) {

    public static ResponseArticle of(Article article, Integer likeNum, Boolean isLiked, Boolean isOwner, Integer commentNum,
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
                .createAt(article.getCreateAt())
                .updatedAt(article.getUpdateAt())
                .commentNum(commentNum)
                .comments(comments)
                .build();
    }
}
