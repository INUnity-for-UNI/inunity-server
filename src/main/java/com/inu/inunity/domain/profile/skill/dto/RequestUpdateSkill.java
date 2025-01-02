package com.inu.inunity.domain.profile.skill.dto;

import com.inu.inunity.domain.profile.skill.SkillLevel;
import com.inu.inunity.domain.profile.skill.SkillType;

public record RequestUpdateSkill(Long skillId, SkillType type, String name, SkillLevel level) {
}
