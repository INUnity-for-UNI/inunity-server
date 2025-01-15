package com.inu.inunity.domain.article;

import jakarta.persistence.Embeddable;

@Embeddable
public class AnonymousList {
    Long id;
    Long userId;

    public AnonymousList() {
    }

    public AnonymousList(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
