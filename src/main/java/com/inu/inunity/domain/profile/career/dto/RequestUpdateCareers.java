package com.inu.inunity.domain.profile.career.dto;

import java.util.List;

public record RequestUpdateCareers(List<RequestCreateCareer> createCareers, List<RequestModifyCareer> modifyCareers, List<Long> deleteCareers) {
}
