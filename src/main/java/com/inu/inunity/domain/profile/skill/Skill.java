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

    private String name;

    private SkillType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
