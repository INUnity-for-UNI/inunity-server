package com.inu.inunity.domain.profile;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotOwnerException;
import com.inu.inunity.domain.profile.career.CareerService;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareer;
import com.inu.inunity.domain.profile.career.dto.ResponseCareer;
import com.inu.inunity.domain.profile.contract.ContractService;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseProfile getProfile(Long userId, UserDetails userDetails){
        User user = userService.findUserById(userId);
        Boolean isOwner =isOwner(userId, userDetails);
        List<ResponseContract> contracts = contractService.getContracts(user);
        return ResponseProfile.of(user, contracts, isOwner);
    }

    @Transactional
    public void updateProfile(RequestUpdateProfile requestUpdateProfile, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
        User user = userService.findUserById(userId);

        user.updateUserProfile(requestUpdateProfile.nickname(), requestUpdateProfile.description(), requestUpdateProfile.graduateDate(), requestUpdateProfile.isGraduation(), requestUpdateProfile.profileImageUrl(),
                requestUpdateProfile.isAnonymous(), requestUpdateProfile.organization(), requestUpdateProfile.job());
        contractService.updateContracts(requestUpdateProfile.contracts(), user);
    }

    @Transactional
    public void createCareer(RequestCreateCareer requestCreateCareer, Long userId, UserDetails userDetails){
        checkOwner(userId, userDetails);
        User user = userService.findUserById(userId);
        careerService.createCareer(requestCreateCareer, user);
    }

    @Transactional
    public List<ResponseCareer> getCareers(Long userId){
        User user = userService.findUserById(userId);
        return careerService.getCareers(user);
    }

    @Transactional
    public void updateCareer(RequestUpdateCareer requestUpdateCareer, Long userId, UserDetails userDetails){
        checkOwner(userId, userDetails);
        careerService.updateCareer(requestUpdateCareer);
    }

    @Transactional
    public void deleteCareer(Long careerId, Long userId, UserDetails userDetails){
        checkOwner(userId, userDetails);
        careerService.deleteCareer(careerId);
    }

    @Transactional
    public void createPortfolio(RequestCreatePortfolio requestCreatePortfolio, Long userId, UserDetails userDetails) {
        checkOwner(userId, userDetails);
        User user = userService.findUserById(userId);
        portfolioService.createPortfolio(requestCreatePortfolio, user);
    }

    @Transactional
    public List<ResponsePortfolio> getPortfolios(Long userId) {
        User user = userService.findUserById(userId);
        return portfolioService.getPortfolios(user);
    }

    @Transactional
    public void updatePortfolio(RequestUpdatePortfolio requestUpdatePortfolio, Long userId, UserDetails userDetails) {
        checkOwner(userId, userDetails);
        portfolioService.updatePortfolio(requestUpdatePortfolio);
    }

    @Transactional
    public void deletePortfolio(Long portfolioId, Long userId, UserDetails userDetails) {
        checkOwner(userId, userDetails);
        portfolioService.deletePortfolio(portfolioId);
    }

    @Transactional
    public void createSkill(RequestCreateSkill requestCreateSkill, Long userId, UserDetails userDetails) {
        checkOwner(userId, userDetails);
        User user = userService.findUserById(userId);
        skillService.createSkill(requestCreateSkill, user);
    }

    @Transactional
    public List<ResponseSkill> getSkills(Long userId) {
        User user = userService.findUserById(userId);
        return skillService.getSkills(user);
    }

    @Transactional
    public void updateSkill(RequestUpdateSkill requestUpdateSkill, Long userId, UserDetails userDetails) {
        checkOwner(userId, userDetails);
        skillService.updateSkill(requestUpdateSkill);
    }

    @Transactional
    public void deleteSkill(Long skillId, Long userId, UserDetails userDetails) {
        Long userTokenId = ((CustomUserDetails) userDetails).getId();
        checkOwner(userId, userDetails);
        skillService.deleteSkill(skillId);
    }

    public void checkOwner(Long userId, UserDetails userDetails) {
        if(!isOwner(userId, userDetails)){
            throw new NotOwnerException(ExceptionMessage.NOT_AUTHORIZATION_ACCESS);
        }
    }

    public Boolean isOwner(Long userId, UserDetails userDetails) {
        if(userDetails != null){
            Long userTokenId = ((CustomUserDetails) userDetails).getId();
            return Objects.equals(userTokenId, userId);
        }
        return false;
    }
}
