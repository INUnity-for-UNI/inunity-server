package com.inu.inunity.domain.profile.career.dto;

import java.time.LocalDate;

public record RequestUpdateCareers(Long careerId, String companyName, String position, LocalDate startDate, LocalDate endDate) {
}
