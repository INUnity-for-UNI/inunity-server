package com.inu.inunity.domain.user.dto;

import java.time.LocalDate;

public record RequestSetUser(String userName, String nickName, LocalDate graduationDate, Boolean isGraduation) {
}
