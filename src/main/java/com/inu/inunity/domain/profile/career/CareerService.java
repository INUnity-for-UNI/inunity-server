package com.inu.inunity.domain.profile.career;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareers;
import com.inu.inunity.domain.profile.career.dto.ResponseCareer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void updateCareers(List<RequestUpdateCareers> requestUpdateCareers, User user){
        List<Career> existingCareers = user.getCareers();
        List<RequestUpdateCareers> careersToCreate = new ArrayList<>();
        List<RequestUpdateCareers> careersToModify = new ArrayList<>();

        Map<Long, Career> skillMap = existingCareers.stream()
                .collect(Collectors.toMap(Career::getId, Career -> Career));

        requestUpdateCareers.forEach(requestUpdateCareer -> {
            if (requestUpdateCareer.careerId() == null) {
                careersToCreate.add(requestUpdateCareer);
            } else {
                careersToModify.add(requestUpdateCareer);
                skillMap.remove(requestUpdateCareer.careerId());
            }
        });

        createCareers(careersToCreate, user);
        modifyCareers(careersToModify);
        deleteCareers(skillMap.keySet().stream().toList());
    }

    private void createCareers(List<RequestUpdateCareers> requestCreateCareers, User user){
        List<Career> careers = requestCreateCareers.stream()
                .map(requestCreateCareer -> Career.of(requestCreateCareer.companyName(), requestCreateCareer.position(),
                        requestCreateCareer.startDate(), requestCreateCareer.endDate(), user))
                .toList();

        careerRepository.saveAll(careers);
    }

    private void modifyCareers(List<RequestUpdateCareers> requestModifyCareers){
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
