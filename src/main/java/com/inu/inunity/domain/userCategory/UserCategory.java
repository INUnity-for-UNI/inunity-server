package com.inu.inunity.domain.userCategory;

import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder
    public UserCategory(Long id, User user, Category category){
        this.id = id;
        this.user = user;
        this.category = category;
    }

    public static UserCategory of(User user, Category category){
        return UserCategory.builder()
                .user(user)
                .category(category)
                .build();
    }
}
