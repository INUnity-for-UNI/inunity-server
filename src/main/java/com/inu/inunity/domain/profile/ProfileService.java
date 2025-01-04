package com.inu.inunity.domain.profile;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotOwnerException;
import com.inu.inunity.domain.profile.career.CareerService;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareer;
import com.inu.inunity.domain.profile.career.dto.ResponseCareer;
import com.inu.inunity.domain.profile.contract.ContractService;
import com.inu.inunity.domain.profile.contract.dto.RequestCreateContract;
import com.inu.inunity.domain.profile.contract.dto.RequestUpdateContract;
import com.inu.inunity.domain.profile.contract.dto.ResponseContract;
import com.inu.inunity.domain.profile.portfolio.PortfolioService;
import com.inu.inunity.domain.profile.portfolio.dto.RequestCreatePortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.RequestUpdatePortfolio;
import com.inu.inunity.domain.profile.portfolio.dto.ResponsePortfolio;
import com.inu.inunity.domain.profile.skill.SkillService;
import com.inu.inunity.domain.profile.skill.dto.RequestCreateSkill;
import com.inu.inunity.domain.profile.skill.dto.RequestUpdateSkill;
import com.inu.inunity.domain.profile.skill.dto.ResponseSkill;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserService;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;
    private final CareerService careerService;
    private final ContractService contractService;
    private final PortfolioService portfolioService;
    private final SkillService skillService;

    public void createCareer(RequestCreateCareer requestCreateCareer, Long userId, UserDetails userDetails){
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        User user = userService.findUserById(userTokenId);
        careerService.createCareer(requestCreateCareer, user);
    }

    public List<ResponseCareer> getCareers(Long userId, UserDetails userDetails){
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        User user = userService.findUserById(userId);

        return careerService.getCareers(user);
    }

    public void updateCareer(RequestUpdateCareer requestUpdateCareer, Long userId, UserDetails userDetails){
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        careerService.updateCareer(requestUpdateCareer);
    }

    public void deleteCareer(Long careerId, Long userId, UserDetails userDetails){
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        careerService.deleteCareer(careerId);
    }

    public void createContract(RequestCreateContract requestCreateContract, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        User user = userService.findUserById(userTokenId);
        contractService.createContract(requestCreateContract, user);
    }

    public List<ResponseContract> getContracts(Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        User user = userService.findUserById(userId);

        return contractService.getContracts(user);
    }

    public void updateContract(RequestUpdateContract requestUpdateContract, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        contractService.updateContract(requestUpdateContract);
    }

    public void deleteContract(Long contractId, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        contractService.deleteContract(contractId);
    }

    public void createPortfolio(RequestCreatePortfolio requestCreatePortfolio, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        User user = userService.findUserById(userTokenId);
        portfolioService.createPortfolio(requestCreatePortfolio, user);
    }

    public List<ResponsePortfolio> getPortfolios(Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        User user = userService.findUserById(userId);

        return portfolioService.getPortfolios(user);
    }

    public void updatePortfolio(RequestUpdatePortfolio requestUpdatePortfolio, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        portfolioService.updatePortfolio(requestUpdatePortfolio);
    }

    public void deletePortfolio(Long portfolioId, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        portfolioService.deletePortfolio(portfolioId);
    }

    public void createSkill(RequestCreateSkill requestCreateSkill, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        User user = userService.findUserById(userTokenId);
        skillService.createSkill(requestCreateSkill, user);
    }

    public List<ResponseSkill> getSkills(Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        User user = userService.findUserById(userId);
        return skillService.getSkills(user);
    }

    public void updateSkill(RequestUpdateSkill requestUpdateSkill, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        skillService.updateSkill(requestUpdateSkill);
    }

    public void deleteSkill(Long skillId, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userTokenId);
        skillService.deleteSkill(skillId);
    }

    public void checkOwner(Long userId, Long userTokenId) {
        log.info(isOwner(userId, userTokenId).toString());
        if(!isOwner(userId, userTokenId)){
            throw new NotOwnerException(ExceptionMessage.NOT_AUTHORIZATION_ACCESS);
        }
    }

    public Boolean isOwner(Long userId, Long userTokenId) {
        return Objects.equals(userTokenId, userId);
    }
}
