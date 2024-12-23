package com.inu.inunity.domain.profile.skill;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.profile.skill.dto.RequestUpdateSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    @Transactional
    public void updateSkills(List<RequestUpdateSkill> requestUpdateSkills, User user) {
        List<Skill> existingSkills = user.getSkills();
        List<RequestUpdateSkill> skillsToCreate = new ArrayList<>();
        List<RequestUpdateSkill> skillsToModify = new ArrayList<>();

        Map<Long, Skill> skillMap = existingSkills.stream()
                .collect(Collectors.toMap(Skill::getId, skill -> skill));

        requestUpdateSkills.forEach(requestSkill -> {
            if (requestSkill.skillId() == null) {
                skillsToCreate.add(requestSkill);
            } else {
                skillsToModify.add(requestSkill);
                skillMap.remove(requestSkill.skillId());
            }
        });

        createSkills(skillsToCreate, user);
        modifySkills(skillsToModify);
        deleteSkills(skillMap.keySet().stream().toList());
    }

    private void createSkills(List<RequestUpdateSkill> createSkills, User user) {
        List<Skill> skills = createSkills.stream()
                .map(req -> new Skill(req.type(), req.name(), req.level(), user))
                .toList();
        skillRepository.saveAll(skills);
    }

    private void modifySkills(List<RequestUpdateSkill> modifySkills) {
        modifySkills.forEach(req -> {
            Skill skill = skillRepository.findById(req.skillId())
                    .orElseThrow(() -> new NotFoundElementException(ExceptionMessage.SKILL_NOT_FOUND));
            skill.update(req.type(), req.name(), req.level());
        });
    }

    private void deleteSkills(List<Long> deleteSkills) {
        skillRepository.deleteAllById(deleteSkills);
    }
}
