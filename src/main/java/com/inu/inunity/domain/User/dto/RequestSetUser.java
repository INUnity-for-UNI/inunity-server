package com.inu.inunity.domain.User.dto;

import lombok.Getter;

import java.time.LocalDate;

public record RequestSetUser(String userName, String nickName, LocalDate graduationDate) {
}
