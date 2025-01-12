package com.inu.inunity.domain.profile.portfolio;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.profile.portfolio.dto.RequestCreatePortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.RequestUpdatePortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.ResponsePortfolio;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Transactional(readOnly = true)
    public List<ResponsePortfolio> getPortfolios(User user){
        return user.getPortfolios().stream()
                .sorted(Comparator.comparing(Portfolio::getStartDate))
                .map(portfolio-> ResponsePortfolio.of(portfolio.getId(), portfolio.getTitle(), portfolio.getUrl(), portfolio.getStartDate(), portfolio.getEndDate()))
                .toList();
    }

    @Transactional
    public void createPortfolio(RequestCreatePortfolio requestCreatePortfolio, User user){
        Portfolio portfolio = Portfolio.of(requestCreatePortfolio.title(), requestCreatePortfolio.url(), requestCreatePortfolio.startDate(),
                requestCreatePortfolio.endDate(), user);

        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void updatePortfolio(RequestUpdatePortfolio requestUpdatePortfolio){
        Portfolio portfolio = portfolioRepository.findById(requestUpdatePortfolio.portfolioId())
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.PORTFOLIO_NOT_FOUND));

        portfolio.update(requestUpdatePortfolio.title(), requestUpdatePortfolio.url(), requestUpdatePortfolio.startDate(), requestUpdatePortfolio.endDate());
    }

    @Transactional
    public void deletePortfolio(Long deleteDeletePortfolio){
        portfolioRepository.deleteById(deleteDeletePortfolio);
    }
}
