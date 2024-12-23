package com.inu.inunity.domain.profile.contract;

import com.inu.inunity.domain.User.User;
import com.inu.inunity.domain.User.UserRepository;
import com.inu.inunity.domain.profile.contract.dto.RequestCreateContract;
import com.inu.inunity.domain.profile.contract.dto.RequestModifyContract;
import com.inu.inunity.domain.profile.contract.dto.RequestUpdateContracts;
import com.inu.inunity.domain.profile.contract.dto.ResponseContract;
import com.inu.inunity.security.exception.ExceptionMessage;
import com.inu.inunity.security.exception.NotFoundElementException;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<ResponseContract> getContracts(User user){
        return user.getContracts().stream()
                .map(contract -> new ResponseContract(contract.getId(), contract.getType(), contract.getName(), contract.getUrl()))
                .toList();
    }

    @Transactional
    public void updateContracts(RequestUpdateContracts requestUpdateContracts, UserDetails userDetails){
        Long userId = ((CustomUserDetails) userDetails).getId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundElementException(ExceptionMessage.USER_NOT_FOUND));

        createContracts(requestUpdateContracts.createContracts(), user);
        modifyContracts(requestUpdateContracts.modifyContracts());
        deleteContracts(requestUpdateContracts.deleteContracts());
    }

    private void createContracts(List<RequestCreateContract> requestCreateContracts, User user){
        List<Contract> contracts = requestCreateContracts.stream()
                .map(createContract -> Contract.of(createContract.name(),createContract.url(), createContract.type(), user))
                .toList();

        contractRepository.saveAll(contracts);
    }

    private void modifyContracts(List<RequestModifyContract> requestModifyContracts){
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