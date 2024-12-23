package com.inu.inunity.domain.profile.career;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestModifyCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareers;
import com.inu.inunity.domain.profile.career.dto.ResponseCareer;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotFoundElementException;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;
    private final UserRepository userRepository;

    public List<ResponseCareer> getCareers(User user){
        return user.getCareers().stream()
                .map(career -> ResponseCareer.of(career.getId(), career.getCompanyName(), career.getPosition(),
                        career.getStartDate(), career.getEndDate()))
                .toList();
    }

    @Transactional
    public void updateCareers(RequestUpdateCareers requestUpdateCareers, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        createCareers(requestUpdateCareers.createCareers(), user);
        modifyCareers(requestUpdateCareers.modifyCareers());
        deleteCareers(requestUpdateCareers.deleteCareers());
    }

    private void createCareers(List<RequestCreateCareer> requestCreateCareers, User user){
        List<Career> careers = requestCreateCareers.stream()
                .map(requestCreateCareer -> Career.of(requestCreateCareer.companyName(), requestCreateCareer.position(),
                        requestCreateCareer.startDate(), requestCreateCareer.endDate(), user))
                .toList();

        careerRepository.saveAll(careers);
    }

    private void modifyCareers(List<RequestModifyCareer> requestModifyCareers){
        requestModifyCareers.forEach(requestModifyCareer -> {
            Career career = careerRepository.findById(requestModifyCareer.careerId())
                    .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.CAREER_NOT_FOUND));

            career.modify(requestModifyCareer.companyName(), requestModifyCareer.position(), requestModifyCareer.startDate(), requestModifyCareer.endDate());
        });
    }

    private void deleteCareers(List<Long> requestDelete){
        careerRepository.deleteAllById(requestDelete);
    }
}
