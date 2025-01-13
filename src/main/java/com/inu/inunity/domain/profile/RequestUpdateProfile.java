package com.inu.inunity.domain.profile;

import com.inu.inunity.domain.profile.contract.dto.RequestCreateUpdateContract;

import java.time.LocalDate;
import java.util.List;

public record RequestUpdateProfile(
        String nickname,
        String description,
        LocalDate graduateDate,
        Boolean isGraduation,
        Boolean isAnonymous,
        String organization,
        String job,
        List<RequestCreateUpdateContract> contracts
) {
}
