package com.inu.inunity.common.fcm;

import com.inu.inunity.common.BaseEntity;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;         // FCM 토큰
    private String deviceInfo;    // 디바이스 정보 (예: Android/iOS 등)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public FcmToken(Long id, String token, String deviceInfo, boolean isActive, User user){
        this.id = id;
        this.token = token;
        this.deviceInfo = deviceInfo;
        this.isActive = isActive;
        this.user = user;
    }

    public static FcmToken of(String token, User user){
        return FcmToken.builder()
                .token(token)
                .user(user)
                .build();
    }
}
