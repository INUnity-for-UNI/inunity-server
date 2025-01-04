package com.inu.inunity.domain.profile.contract;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.profile.contract.dto.RequestCreateContract;
import com.inu.inunity.domain.profile.contract.dto.RequestUpdateContract;
import com.inu.inunity.domain.profile.contract.dto.ResponseContract;
import com.inu.inunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    @Transactional(readOnly = true)
    public List<ResponseContract> getContracts(User user){
        return user.getContracts().stream()
                .map(contract -> new ResponseContract(contract.getId(), contract.getType(), contract.getName(), contract.getUrl()))
                .toList();
    }

    @Transactional
    public void createContract(RequestCreateContract requestCreateContract, User user){
        Contract contract = Contract.of(requestCreateContract.name(),requestCreateContract.url(), requestCreateContract.type(), user);
        contractRepository.save(contract);
    }

    @Transactional
    public void updateContract(RequestUpdateContract requestModifyContract){
        Contract contract = contractRepository.findById(requestModifyContract.contractId())
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.CONTRACT_NOT_FOUND));
        contract.edit(requestModifyContract.name(), requestModifyContract.url(), requestModifyContract.type());
    }

    @Transactional
    public void deleteContract(Long requestDeleteContract){
        contractRepository.deleteById(requestDeleteContract);
    }
}