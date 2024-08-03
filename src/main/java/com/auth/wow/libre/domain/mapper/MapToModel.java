package com.auth.wow.libre.domain.mapper;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

public class MapToModel {

    public static AccountWebModel accountWebService(AccountWebEntity accountWebEntity) {
        return new AccountWebModel(accountWebEntity.getId(), accountWebEntity.getCountry(),
                accountWebEntity.getDateOfBirth(),
                accountWebEntity.getFirstName(), accountWebEntity.getLastName(), accountWebEntity.getCellPhone(),
                accountWebEntity.getEmail(), accountWebEntity.getPassword(), accountWebEntity.getRolId().getId(),
                accountWebEntity.getRolId().getName(), accountWebEntity.getStatus(), accountWebEntity.getVerified(),
                accountWebEntity.getAvatarUrl());
    }

}
