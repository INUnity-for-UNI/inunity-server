package com.inu.inunity.common.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserRepository;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendMessage(com.inu.inunity.common.fcm.notification.Notification notification) {
            List<FcmToken> tokens = notification.getUser().getFcmTokens();
            tokens.forEach(token -> {
                        try {
                            Message message = Message.builder()
                                    .setToken(token.getToken())
                                    .setNotification(Notification.builder()
                                            .setTitle(notification.getTitle())
                                            .setBody(notification.getContent())
                                            .build())
                                    .build();
                            String response = FirebaseMessaging.getInstance().send(message);
                            System.out.println("FCM 메시지 전송 성공: " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
    }

    public void saveFcmToken(String fcmToken, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        if(!fcmTokenRepository.existsByToken(fcmToken)){
            fcmTokenRepository.save(FcmToken.of(fcmToken, user));
        }
    }
}
