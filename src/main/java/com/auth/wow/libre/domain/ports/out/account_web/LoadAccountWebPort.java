package com.auth.wow.libre.domain.ports.out.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

public interface LoadAccountWebPort {
    AccountWebEntity save(Account account);

}
