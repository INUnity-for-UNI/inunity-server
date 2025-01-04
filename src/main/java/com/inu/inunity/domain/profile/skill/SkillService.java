package com.inu.inunity.domain.profile.skill;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.profile.skill.dto.RequestCreateSkill;
import com.inu.inunity.domain.profile.skill.dto.RequestUpdateSkill;
import com.inu.inunity.domain.profile.skill.dto.ResponseSkill;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    @Transactional(readOnly = true)
    public List<ResponseSkill> getSkills(User user){
        return user.getSkills().stream()
                .map(skill -> ResponseSkill.of(skill.getId(), skill.getType(), skill.getName(), skill.getLevel()))
                .toList();
    }

    @Transactional
    public void createSkill(RequestCreateSkill requestCreateSkill, User user) {
        Skill skill = new Skill(requestCreateSkill.type(), requestCreateSkill.name(), requestCreateSkill.level(), user);
        skillRepository.save(skill);
    }

    @Transactional
    public void updateSkill(RequestUpdateSkill requestUpdateSkill) {
        Skill skill = skillRepository.findById(requestUpdateSkill.skillId())
                .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.SKILL_NOT_FOUND));
        skill.update(requestUpdateSkill.type(), requestUpdateSkill.name(), requestUpdateSkill.level());
    }

    @Transactional
    public void deleteSkill(Long requestDeleteSkill) {
        skillRepository.deleteById(requestDeleteSkill);
    }
}
