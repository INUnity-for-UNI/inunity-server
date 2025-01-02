package com.inu.inunity.domain.profile.portfolio.dto;

import java.time.LocalDate;

public record RequestPortfolio(Long portfolioId, String url, LocalDate startDate, LocalDate endDate) {
}
