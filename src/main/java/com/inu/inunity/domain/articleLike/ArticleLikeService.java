package com.inu.inunity.domain.articleLike;

import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;

    @Transactional(readOnly = true)
    public Integer getLikeNum(Article article){
        return articleLikeRepository.countByArticleId(article.getId());
    }

    @Transactional
    public Integer toggleLike(Article article, User user) {
        Optional<ArticleLike> optionalLike = articleLikeRepository.findByArticleAndUser(article, user);

        if (optionalLike.isPresent()) {
            articleLikeRepository.delete(optionalLike.get());
        } else {
            ArticleLike articleLike = new ArticleLike(article, user);
            articleLikeRepository.save(articleLike);
        }

        return getLikeNum(article);
    }

    @Transactional(readOnly = true)
    public Boolean isLike(Long articleId, Long userId){
        return articleLikeRepository.existsArticleLikeByArticleIdAndUserId(articleId, userId);
    }
}
