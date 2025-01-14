package com.inu.inunity.common.fcm.notification;

import com.inu.inunity.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/v1/notification")
    public CommonResponse<?> getNotifications(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        return CommonResponse.success("유저의 알람 가져오기 성공",
                notificationService.getUsersAllNotification(userDetails, pageable));
    }

    @PutMapping("/v1/notification/{notificationid}")
    public void markNotificationAsRead(@PathVariable Long notificationid, UserDetails userDetails) {
        notificationService.updateRead(notificationid, userDetails);
    }
}