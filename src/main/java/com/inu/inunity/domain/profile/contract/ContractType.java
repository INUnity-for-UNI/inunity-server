package com.inu.inunity.domain.profile.contract;

public enum ContractType {
    SNS(0),
    WEBSITE(1),
    ETC(2);

    private Integer code;

    ContractType(Integer code) {
        this.code = code;
    }
}
