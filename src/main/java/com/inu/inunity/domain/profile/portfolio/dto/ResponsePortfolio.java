package com.inu.inunity.domain.profile.portfolio.dto;

import java.time.LocalDate;

public record ResponsePortfolio(Long portfolioId, String title, String url, LocalDate startDate, LocalDate endDate) {
    public static ResponsePortfolio of(Long portfolioId, String title, String url, LocalDate startDate, LocalDate endDate){
        return new ResponsePortfolio(portfolioId, title, url, startDate, endDate);
    }
}
