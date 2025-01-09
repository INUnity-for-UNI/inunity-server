package com.inu.inunity.domain.profile.portfolio.dto;

import java.time.LocalDate;

public record RequestUpdatePortfolio(Long portfolioId, String url, LocalDate startDate, LocalDate endDate) {
}
