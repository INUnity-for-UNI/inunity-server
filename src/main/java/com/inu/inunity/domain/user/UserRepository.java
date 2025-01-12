package com.inu.inunity.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStudentId(Long studentId);

    boolean existsByStudentId(Long studentId);

}
