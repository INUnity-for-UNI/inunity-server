package com.inu.inunity.domain.userCategory;

import com.inu.inunity.domain.category.Category;
import com.inu.inunity.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    Optional<UserCategory> findByUserAndCategory(User user, Category category);
    boolean existsByUserAndCategory(User user, Category category);
}
