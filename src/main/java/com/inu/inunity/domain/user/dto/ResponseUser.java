package com.inu.inunity.domain.user.dto;

import com.inu.inunity.domain.user.User;
import lombok.Builder;

@Builder
public record ResponseUser(Long id,
                           Long studentId,
                           String nickname,
                           String department,
                           String profileImageUrl,
                           Boolean isAnonymous,
                           String organization,
                           String job
) {
  public static ResponseUser of(User user){
      return ResponseUser.builder()
              .id(user.getId())
              .nickname(user.getNickname())
              .department(user.getDepartment())
              .profileImageUrl(user.getProfileImageUrl())
              .isAnonymous(user.getIsAnonymous())
              .organization(user.getOrganization())
              .job(user.getJob())
              .build();
  }
}
