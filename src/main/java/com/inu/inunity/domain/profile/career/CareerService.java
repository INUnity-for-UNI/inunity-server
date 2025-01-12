package com.inu.inunity.domain.profile.career;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.profile.career.dto.RequestCreateCareer;
import com.inu.inunity.domain.profile.career.dto.RequestUpdateCareer;
import com.inu.inunity.domain.profile.career.dto.ResponseCareer;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;

    @Transactional(readOnly = true)
    public List<ResponseCareer> getCareers(User user){
        return user.getCareers().stream()
                .map(career -> ResponseCareer.of(career.getId(), career.getCompanyName(), career.getPosition(),
                        career.getStartDate(), career.getEndDate()))
                .toList();
    }

    @Transactional
    public void createCareer(RequestCreateCareer requestCreateCareer, User user){
        Career career = Career.of(requestCreateCareer.companyName(), requestCreateCareer.position(),
                requestCreateCareer.startDate(), requestCreateCareer.endDate(), user);

        careerRepository.save(career);
    }

    @Transactional
    public void updateCareer(RequestUpdateCareer requestUpdateCareer){
        Career career = careerRepository.findById(requestUpdateCareer.careerId())
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.CAREER_NOT_FOUND));

        career.modify(requestUpdateCareer.companyName(), requestUpdateCareer.position(),
                requestUpdateCareer.startDate(), requestUpdateCareer.endDate());
    }

    @Transactional
    public void deleteCareer(Long requestDelete){
        careerRepository.deleteById(requestDelete);
    }
}
