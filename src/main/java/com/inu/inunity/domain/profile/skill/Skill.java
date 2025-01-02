package com.inu.inunity.domain.profile.skill;

import com.inu.inunity.domain.User.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private SkillType type;

    private String name;

    private SkillLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Skill(SkillType type, String name, SkillLevel level, User user) {
        this.type = type;
        this.name = name;
        this.level = level;
        this.user = user;
    }

    public static Skill of(SkillType type, String name, SkillLevel level, User user){
        return new Skill(type, name, level, user);
    }

    public Skill update(SkillType type, String name, SkillLevel level){
        this.type = type;
        this.name = name;
        this.level = level;

        return this;
    }
}
