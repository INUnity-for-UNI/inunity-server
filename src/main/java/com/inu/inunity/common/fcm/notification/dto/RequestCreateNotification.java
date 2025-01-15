package com.inu.inunity.common.fcm.notification.dto;

import com.inu.inunity.common.fcm.notification.NotificationType;

public record RequestCreateNotification(
        NotificationType type,
        Long targetId,
        String title,
        String content,
        boolean isPushed,
        boolean isRead
        ) {
}
