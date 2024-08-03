package com.auth.wow.libre.domain.ports.in.account_web;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.security.JwtDto;

public interface AccountWebPort {

    AccountWebModel save(AccountWebModel accountWebModel, RolModel rol, String transactionId);

    AccountWebModel findByEmail(String email, String transactionId);

    JwtDto create(AccountWebDto accountWebDto, String transactionId);
}
