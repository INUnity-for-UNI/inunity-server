package com.inu.inunity.domain.profile.skill.dto;

import com.inu.inunity.domain.profile.skill.SkillLevel;
import com.inu.inunity.domain.profile.skill.SkillType;

public record ResponseSkill(Long skillId, SkillType type, String name, SkillLevel level) {
    public static ResponseSkill of(Long skillId, SkillType type, String name, SkillLevel level){
        return new ResponseSkill(skillId, type, name, level);
    }
}
