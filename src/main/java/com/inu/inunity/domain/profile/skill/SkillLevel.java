package com.inu.inunity.domain.profile.skill;

public enum SkillLevel {
    LOW("하"),
    MEDIUM("중"),
    HIGH("상");

    private final String label;

    SkillLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
