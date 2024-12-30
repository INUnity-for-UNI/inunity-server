package com.inu.inunity.domain.profile.contract.dto;

import com.inu.inunity.domain.profile.contract.ContractType;

public record RequestCreateContract(ContractType type, String name, String url) {
}
