package com.inu.inunity.domain.profile.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    @Modifying
    @Query("DELETE FROM Contract c WHERE c.id IN :ids")
    void deleteAllById( List<Long> ids);
}
