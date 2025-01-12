package com.inu.inunity.domain.profile;

import com.inu.inunity.domain.profile.contract.dto.ResponseContract;
import com.inu.inunity.domain.user.User;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ResponseProfile(
        String nickname,
        LocalDate graduateDate,
        Boolean isGraduation,
        Boolean isAnonymous,
        String organization,
        String job,
        Boolean isOwner,
        List<ResponseContract> contracts
        ) {

    public static ResponseProfile of(User user, List<ResponseContract> contracts, Boolean isOwner) {
        return ResponseProfile.builder()
                .nickname(user.getNickname())
                .graduateDate(user.getGraduateDate())
                .isGraduation(user.getIsGraduation())
                .isAnonymous(user.getIsAnonymous())
                .organization(user.getOrganization())
                .job(user.getJob())
                .isOwner(isOwner)
                .contracts(contracts)
                .build();
    }
}
