package com.inu.inunity.domain.category;

import com.inu.inunity.domain.article.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String icon;

    private Boolean isActive;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @Builder
    public Category(String name, String description, String icon,Boolean isActive) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.isActive = isActive;
    }

    public void updateCategory(String name, String description, String icon, Boolean isActive) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.isActive = isActive;
    }

    public void changeStatus(Boolean status) {
        this.isActive = status;
    }
}
