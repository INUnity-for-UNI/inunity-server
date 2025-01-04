package com.inu.inunity.domain.profile.portfolio.dto;

import java.time.LocalDate;

public record RequestCreatePortfolio(String url, LocalDate startDate, LocalDate endDate) {
}
