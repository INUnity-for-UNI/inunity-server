package com.inu.inunity.domain.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
    SELECT n FROM Notice n
    JOIN n.department d
    WHERE d.name LIKE CONCAT('%', :departmentName, '%')
    """)
    Page<Notice> findAllByDepartmentName(@Param("departmentName") String departmentName, Pageable pageable);

}