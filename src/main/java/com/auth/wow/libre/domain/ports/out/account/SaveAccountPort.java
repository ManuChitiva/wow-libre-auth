package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.infrastructure.entities.AccountEntity;

public interface SaveAccountPort {
    void save(AccountEntity account);
}
