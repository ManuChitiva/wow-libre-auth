package com.auth.wow.libre.application.services.account_banned;

import com.auth.wow.libre.domain.model.AccountBanned;
import com.auth.wow.libre.domain.ports.in.account_banned.AccountBannedPort;
import com.auth.wow.libre.domain.ports.out.account_banned.ObtainAccountBannedPort;
import com.auth.wow.libre.infrastructure.entities.AccountBannedEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountBannedService implements AccountBannedPort {
    private final ObtainAccountBannedPort obtainAccountBannedPort;

    public AccountBannedService(ObtainAccountBannedPort obtainAccountBannedPort) {
        this.obtainAccountBannedPort = obtainAccountBannedPort;
    }

    @Override
    public AccountBanned getAccountBanned(Long accountId) {
        return obtainAccountBannedPort.getAccountBanned(accountId).map(this::mapToModel).orElse(null);
    }

    private AccountBanned mapToModel(AccountBannedEntity accountBannedEntity) {
        return new AccountBanned(
                accountBannedEntity.getAccountId(),
                new java.util.Date(accountBannedEntity.getBandate() * 1000),
                new java.util.Date(accountBannedEntity.getUnbandate() * 1000),
                accountBannedEntity.getBannedby(),
                accountBannedEntity.getBanreason(),
                accountBannedEntity.getActive() == 1
        );
    }
}
