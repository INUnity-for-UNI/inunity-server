package com.inu.inunity.domain.profile.contract;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.profile.contract.dto.RequestUpdateContract;
import com.inu.inunity.domain.profile.contract.dto.ResponseContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void updateContracts(List<RequestUpdateContract> requestUpdateContracts, User user){
        List<Contract> existingContracts = user.getContracts();
        List<RequestUpdateContract> contractsToCreate = new ArrayList<>();
        List<RequestUpdateContract> contractsToModify = new ArrayList<>();

        Map<Long, Contract> skillMap = existingContracts.stream()
                .collect(Collectors.toMap(Contract::getId, Contract -> Contract));

        requestUpdateContracts.forEach(requestUpdateCareer -> {
            if (requestUpdateCareer.contractId() == null) {
                contractsToCreate.add(requestUpdateCareer);
            } else {
                contractsToModify.add(requestUpdateCareer);
                skillMap.remove(requestUpdateCareer.contractId());
            }
        });

        createContracts(contractsToCreate, user);
        modifyContracts(contractsToModify);
        deleteContracts(skillMap.keySet().stream().toList());
    }

    private void createContracts(List<RequestUpdateContract> requestCreateContracts, User user){
        List<Contract> contracts = requestCreateContracts.stream()
                .map(createContract -> Contract.of(createContract.name(),createContract.url(), createContract.type(), user))
                .toList();

        contractRepository.saveAll(contracts);
    }

    private void modifyContracts(List<RequestUpdateContract> requestModifyContracts){
        requestModifyContracts.forEach(modifyContract -> {
            Contract contract = contractRepository.findById(modifyContract.contractId())
                    .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.CONTRACT_NOT_FOUND));
            contract.edit(modifyContract.name(), modifyContract.url(), modifyContract.type());
        });
    }

    private void deleteContracts(List<Long> requestDeleteContracts){
        contractRepository.deleteAllById(requestDeleteContracts);
    }
}