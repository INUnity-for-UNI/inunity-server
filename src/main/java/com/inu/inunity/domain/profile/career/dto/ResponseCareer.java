package com.inu.inunity.domain.profile.career.dto;

import java.time.LocalDate;

public record ResponseCareer(Long careerId, String companyName, String position, LocalDate startDate, LocalDate endDate) {
    public static ResponseCareer of(Long careerId, String companyName, String position, LocalDate startDate, LocalDate endDate) {
        return new ResponseCareer(careerId, companyName, position, startDate, endDate);
    }
}
