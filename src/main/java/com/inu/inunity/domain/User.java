package com.inu.inunity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long studentNumber;
    private String nickname;
    private String description;
    private Boolean isGraduation;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> roles = new ArrayList<>();

}
