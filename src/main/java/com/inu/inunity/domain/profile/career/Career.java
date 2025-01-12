package com.inu.inunity.domain.profile.career;

import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Career(Long id, String companyName, String position, LocalDate startDate, LocalDate endDate, User user){
        this.id = id;
        this.companyName = companyName;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public static Career of(String companyName, String position, LocalDate startDate, LocalDate endDate, User user){
        return Career.builder()
                .companyName(companyName)
                .position(position)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .build();
    }

    public void modify(String companyName, String position, LocalDate startDate, LocalDate endDate){
        this.companyName = companyName;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
