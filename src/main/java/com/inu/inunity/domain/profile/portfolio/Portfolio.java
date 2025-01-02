package com.inu.inunity.domain.profile.portfolio;

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
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Portfolio(Long id, String url, LocalDate startDate, LocalDate endDate, User user){
        this.id = id;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public static Portfolio of(String url, LocalDate startDate, LocalDate endDate, User user){
        return Portfolio.builder()
                .url(url)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .build();
    }

    public void update(String url, LocalDate startDate, LocalDate endDate){
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
