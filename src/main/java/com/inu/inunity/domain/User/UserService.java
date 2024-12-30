package com.inu.inunity.domain.User;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.dto.RequestSetUser;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void setUser(RequestSetUser request, UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        user.setUser(request.userName(), request.nickName(), request.graduationDate(), request.isGraduation());
    }
}
