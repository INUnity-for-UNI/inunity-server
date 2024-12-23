package com.inu.inunity.domain.profile.contract.dto;

import com.inu.inunity.domain.profile.contract.Contract;
import com.inu.inunity.domain.profile.contract.ContractType;

public record ResponseContract(Long contractId, ContractType type, String name, String url) {
    public static ResponseContract of(Long contractId, ContractType type, String name, String url) {
        return new ResponseContract(contractId, type, name, url);
    }
}
