package com.inu.inunity.domain.User.dto;

import lombok.Getter;

import java.time.LocalDate;

public record RequestUpdateUser(String nickName, LocalDate graduationDate) {
}
