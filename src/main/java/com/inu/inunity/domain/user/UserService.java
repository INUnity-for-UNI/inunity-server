package com.inu.inunity.domain.user;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.article.ArticleService;
import com.inu.inunity.domain.article.dto.ResponseArticleThumbnail;
import com.inu.inunity.domain.comment.dto.ResponseMyPageComment;
import com.inu.inunity.domain.user.dto.RequestSetUser;
import com.inu.inunity.domain.user.dto.ResponseUser;
import com.inu.inunity.security.Role;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ResponseArticleThumbnail> getUserWroteArticle(UserDetails userDetails, Pageable pageable){
        Long userId = ((CustomUserDetails) userDetails).getId();
        return articleService.getUserWroteArticles(userId, pageable);
    }

    public Page<ResponseArticleThumbnail> getUserLikedArticle(UserDetails userDetails, Pageable pageable){
        Long userId = ((CustomUserDetails) userDetails).getId();
        return articleService.getUserLikedArticles(userId, pageable);
    }

    public List<ResponseMyPageComment> getUserComments(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        return articleService.getUserWroteComments(user);
    }

    public ResponseUser getUserAtUserDetails(UserDetails userDetails){
        User user = findUserById(((CustomUserDetails) userDetails).getId());

        return ResponseUser.of(user);
    }

    @Transactional
    public void editRole(Role role, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = findUserById(userId);
        user.updateAuthenticationForAdmin(List.of(role));
    }
}
