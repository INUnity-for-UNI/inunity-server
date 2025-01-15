package com.inu.inunity.domain.advertise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    Page<Advertise> findAllByOrderByUpdateAtDesc(Pageable pageable);
}
