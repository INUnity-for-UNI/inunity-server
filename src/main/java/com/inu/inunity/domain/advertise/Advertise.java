package com.inu.inunity.domain.advertise;

import com.inu.inunity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String url;

    @Builder
    public Advertise(Long id, String title, String content, String url){
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public static Advertise of(String title, String content, String url){
        return Advertise.builder()
                .title(title)
                .content(content)
                .url(url)
                .build();
    }

    public void editAdvertise(String title, String content, String url){
        this.title = title;
        this.content = content;
        this.url = url;
    }
}
