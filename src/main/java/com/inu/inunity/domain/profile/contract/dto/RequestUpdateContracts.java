package com.inu.inunity.domain.profile.contract.dto;

import java.util.List;

public record RequestUpdateContracts(List<RequestCreateContract> createContracts, List<RequestModifyContract> modifyContracts, List<Long> deleteContracts) {
}
