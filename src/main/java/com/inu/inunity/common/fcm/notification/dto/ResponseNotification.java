package com.inu.inunity.common.fcm.notification.dto;

import com.inu.inunity.common.fcm.notification.NotificationType;
import lombok.Builder;

@Builder
public record ResponseNotification (
        Long id,
        NotificationType type,
        Long categoryId,
        Long articleId,
        String title,
        String content,
        boolean isPushed,
        boolean isRead
) {

    public static ResponseNotification of(Long id, NotificationType type, Long categoryId, Long articleId, String title, String content, boolean isPushed, boolean isRead){
        return ResponseNotification.builder()
                .id(id)
                .type(type)
                .categoryId(categoryId)
                .articleId(articleId)
                .title(title)
                .content(content)
                .isPushed(isPushed)
                .isRead(isRead)
                .build();
    }
}
