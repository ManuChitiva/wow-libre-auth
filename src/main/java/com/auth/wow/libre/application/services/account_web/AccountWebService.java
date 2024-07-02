package com.auth.wow.libre.application.services.account_web;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.SaveAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import com.auth.wow.libre.infrastructure.entities.RolEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountWebService implements AccountWebPort {

    private final SaveAccountWebPort saveAccountWebPort;
    private final ObtainAccountWebPort obtainAccountWebPort;

    public AccountWebService(SaveAccountWebPort saveAccountWebPort,
                             ObtainAccountWebPort obtainAccountWebPort) {
        this.saveAccountWebPort = saveAccountWebPort;
        this.obtainAccountWebPort = obtainAccountWebPort;
    }


    @Override
    public AccountWebModel save(AccountWebModel accountWebModel, RolModel rol, String transactionId) {

        final AccountWebEntity accountWebEntity = AccountWebEntity.create(accountWebModel,
                RolEntity.mapToAccountRolEntity(rol));

        return mapToModel(saveAccountWebPort.save(accountWebEntity, transactionId));
    }

    @Override
    public AccountWebModel findByEmail(String email, String transactionId) {
        return obtainAccountWebPort.findByEmailAndStatusIsTrue(email).map(this::mapToModel).orElse(null);
    }

    private AccountWebModel mapToModel(AccountWebEntity accountWebEntity) {
        return new AccountWebModel(accountWebEntity.getId(), accountWebEntity.getCountry(),
                accountWebEntity.getDateOfBirth(),
                accountWebEntity.getFirstName(), accountWebEntity.getLastName(), accountWebEntity.getCellPhone(),
                accountWebEntity.getEmail(), accountWebEntity.getPassword(), accountWebEntity.getRolId().getId(),
                accountWebEntity.getRolId().getName(), accountWebEntity.getStatus(), accountWebEntity.getVerified(),
                accountWebEntity.getAvatarUrl());
    }

}
