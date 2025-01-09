package com.inu.inunity.domain.user;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.article.ArticleService;
import com.inu.inunity.domain.article.dto.ResponseArticleThumbnail;
import com.inu.inunity.domain.comment.dto.ResponseMyPageComment;
import com.inu.inunity.domain.user.dto.RequestSetUser;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArticleService articleService;

    @Transactional
    public void setUser(RequestSetUser request, UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        user.setUser(request.userName(), request.nickName(), request.graduationDate(), request.isGraduation());
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));
    }

    public List<ResponseArticleThumbnail> getUserWroteArticle(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        return articleService.getUserWroteArticles(user);
    }

    public List<ResponseArticleThumbnail> getUserLikedArticle(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        return articleService.getUserLikedArticles(user);
    }

    public List<ResponseMyPageComment> getUserComments(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        return articleService.getUserWroteComments(user);
    }
}
