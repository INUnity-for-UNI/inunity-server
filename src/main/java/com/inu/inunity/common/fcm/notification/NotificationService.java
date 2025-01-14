package com.inu.inunity.common.fcm.notification;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.common.exception.NotOwnerException;
import com.inu.inunity.common.fcm.FcmToken;
import com.inu.inunity.common.fcm.FcmTokenService;
import com.inu.inunity.common.fcm.notification.dto.ResponseNotification;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmTokenService fcmTokenService;

    @Transactional
    public void createNotification(User user, NotificationType type, Long categoryId, Long articleId, String title, String content, boolean push, List<FcmToken> token) {
        Notification notification = Notification.of(type, categoryId, articleId, title, content, push, false, user);
        notificationRepository.save(notification);
        fcmTokenService.sendMessage(notification);
    }

    @Transactional
    public Page<ResponseNotification> getUsersAllNotification(UserDetails userDetails, Pageable pageable){
        Long userId = ((CustomUserDetails) userDetails).getId();

        return notificationRepository.findAllByUserId(userId, pageable)
                .map(notification -> ResponseNotification.of(notification.getId(), notification.getType(), notification.getCategoryId(), notification.getArticleId(), notification.getTitle(), notification.getContent(), notification.isPushed(), notification.isRead()));
    }


    @Transactional
    public void updateRead(Long notificationId, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.ARTICLE_LIKE_NOT_FOUND));

        if(!Objects.equals(notification.getUser().getId(), userId)){
            throw new NotOwnerException(ExceptionMessage.NOT_AUTHORIZATION_ACCESS);
        }

        notification.updateRead();
    }

}
