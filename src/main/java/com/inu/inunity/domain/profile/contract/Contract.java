package com.inu.inunity.domain.profile.contract;

import com.inu.inunity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private ContractType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Contract(Long id, String name, String url, ContractType type, User user){
        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
        this.user = user;
    }

    public static Contract of(String name, String url, ContractType type, User user){
        return Contract.builder()
                .name(name)
                .url(url)
                .type(type)
                .user(user)
                .build();
    }

    public Contract edit(String name, String url, ContractType type){
        this.name = name;
        this.url = url;
        this.type = type;

        return this;
    }

}
