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
import com.inu.inunity.domain.profile.skill.SkillService;
import com.inu.inunity.domain.user.User;
import com.inu.inunity.domain.user.UserService;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
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

    public List<ResponseCareer> getCareers(UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();
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

    public List<ResponseContract> getContracts(UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getId();
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

    public void checkOwner(Long userId, Long userTokenId){
        if(!isOwner(userId, userTokenId)){
            throw new NotOwnerException(ExceptionMessage.NOT_AUTHORIZATION_ACCESS);
        }
    }

    public Boolean isOwner(Long userId, Long userTokenId){
        return Objects.equals(userId, userTokenId);
    }
}
