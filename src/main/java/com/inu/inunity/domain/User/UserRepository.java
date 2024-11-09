package com.inu.inunity.domain.User;

import com.inu.inunity.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
