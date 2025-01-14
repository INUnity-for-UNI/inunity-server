package com.inu.inunity.domain.userCategory;

import com.inu.inunity.common.fcm.notification.NotificationService;
import com.inu.inunity.common.fcm.notification.NotificationType;
import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;
    private final NotificationService notificationService;

    //오전 9시 오후 12시 오흐 5시 오후 10시
    @Scheduled(cron = "0 0 9,12,17,22 * * ?")
    public void runScheduledTask() {
        List<UserCategory> userCategories =  userCategoryRepository.findAll();

        userCategories
                .forEach(userCategory -> notificationService.createNotification(userCategory.getUser(),
                        NotificationType.article, userCategory.getCategory().getId(), null, userCategory.getCategory().getName() + " 에 최신 글이 등록되었습니다.", "지금 바로 확인해보세요!",
                        true, userCategory.getUser().getFcmTokens()));
    }

    @Transactional
    public boolean toggleUserCategory(Category category, User user) {
        Optional<UserCategory> optionalUserCategory = userCategoryRepository.findByUserAndCategory(user, category);

        if (optionalUserCategory.isPresent()) {
            userCategoryRepository.delete(optionalUserCategory.get());
            return false;
        } else {
            UserCategory userCategory = UserCategory.of(user, category);
            userCategoryRepository.save(userCategory);
            return true;
        }
    }

    @Transactional
    public void createArticleUser(Category category, User user) {
        Optional<UserCategory> optionalUserCategory = userCategoryRepository.findByUserAndCategory(user, category);

        if(optionalUserCategory.isEmpty()){
            userCategoryRepository.save(UserCategory.of(user, category));
        }
    }
}
