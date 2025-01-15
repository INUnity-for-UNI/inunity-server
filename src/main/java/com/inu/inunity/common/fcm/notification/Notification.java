package com.inu.inunity.common.fcm.notification;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private Long categoryId;
    private Long articleId;
    private boolean isRead;
    private boolean isPushed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(Long id, Long categoryId, Long articleId, NotificationType type, String title, String content, boolean isPushed, boolean isRead, User user) {
        this.id = id;
        this.type = type;
        this.categoryId = categoryId;
        this.articleId = articleId;
        this.content = content;
        this.title = title;
        this.isPushed = isPushed;
        this.isRead = isRead;
        this.user = user;
    }

    public static Notification of(NotificationType type, Long categoryId, Long articleId, String title, String content, boolean isPushed, boolean isRead, User user){
        return Notification.builder()
                .type(type)
                .title(title)
                .content(content)
                .articleId(articleId)
                .categoryId(categoryId)
                .isPushed(isPushed)
                .isRead(isRead)
                .user(user)
                .build();
    }

    public void updateRead(){
        this.isRead = true;
    }
}
