package com.inu.inunity.domain.articleUser;

import com.inu.inunity.common.fcm.notification.NotificationService;
import com.inu.inunity.common.fcm.notification.NotificationType;
import com.inu.inunity.domain.article.Article;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleUserService {

    private final ArticleUserRepository articleUserRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean toggleArticleUser(Article article, User user) {
        Optional<ArticleUser> optionalArticleUser = articleUserRepository.findByArticleAndUser(article, user);

        if (optionalArticleUser.isPresent()) {
            articleUserRepository.delete(optionalArticleUser.get());
            return false;
        } else {
            ArticleUser articleUser = ArticleUser.of(user, article);
            articleUserRepository.save(articleUser);
            return true;
        }
    }

    @Transactional
    public void createArticleUser(Article article, User user) {
        Optional<ArticleUser> optionalArticleUser = articleUserRepository.findByArticleAndUser(article, user);

        if(optionalArticleUser.isEmpty()){
            articleUserRepository.save(ArticleUser.of(user, article));
        }
    }

    @Transactional
    public void sendNotification(Article article){
        article.getArticleUsers().forEach(
                articleUser -> notificationService.createNotification(articleUser.getUser(), NotificationType.comment,
                        articleUser.getArticle().getCategory().getId(), articleUser.getArticle().getId(),
                        article.getTitle() + " 글에 새로운 댓글이 달렸습니다.", "지금 바로 확인해보세요!", true,
                        articleUser.getUser().getFcmTokens())
        );
    }
}
