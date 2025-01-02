package com.inu.inunity.domain.profile.portfolio;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.profile.portfolio.dto.RequestPortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.ResponsePortfolio;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<ResponsePortfolio> getPortfolios(User user){
        return user.getPortfolios().stream()
                .sorted(Comparator.comparing(Portfolio::getStartDate))
                .map(portfolio-> ResponsePortfolio.of(portfolio.getId(), portfolio.getUrl(), portfolio.getStartDate(), portfolio.getEndDate()))
                .toList();
    }

    public void updatePortfolios(List<RequestPortfolio> requestUpdatePortfolios, User user){
        List<Portfolio> existingPortfolios = user.getPortfolios();
        List<RequestPortfolio> portfolioToCreate = new ArrayList<>();
        List<RequestPortfolio> portfoliosToModify = new ArrayList<>();

        Map<Long, Portfolio> skillMap = existingPortfolios.stream()
                .collect(Collectors.toMap(Portfolio::getId, Portfolio -> Portfolio));

        requestUpdatePortfolios.forEach(requestPortfolio -> {
            if (requestPortfolio.portfolioId() == null) {
                portfolioToCreate.add(requestPortfolio);
            } else {
                portfoliosToModify.add(requestPortfolio);
                skillMap.remove(requestPortfolio.portfolioId());
            }
        });

        createPortfolio(portfolioToCreate, user);
        modifyPortfolio(portfoliosToModify);
        deletePortfolio(skillMap.keySet().stream().toList());
    }

    public void createPortfolio(List<RequestPortfolio> requestCreatePortfolios, User user){
        List<Portfolio> portfolios = requestCreatePortfolios.stream()
                .map(requestPortfolio -> Portfolio.of(requestPortfolio.url(), requestPortfolio.startDate(), requestPortfolio.endDate(), user))
                .toList();

        portfolioRepository.saveAll(portfolios);
    }

    public void modifyPortfolio(List<RequestPortfolio> requestModifyPortfolios){
        requestModifyPortfolios.forEach(requestPortfolio -> {
            Portfolio portfolio = portfolioRepository.findById(requestPortfolio.portfolioId())
                    .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.PORTFOLIO_NOT_FOUND));

            portfolio.update(requestPortfolio.url(), requestPortfolio.startDate(), requestPortfolio.endDate());
        });
    }

    public void deletePortfolio(List<Long> deleteDeletePortfolios){
        portfolioRepository.deleteAllById(deleteDeletePortfolios);
    }
}
