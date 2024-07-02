package com.auth.wow.libre.domain.ports.out.account_banned;

import com.auth.wow.libre.infrastructure.entities.AccountBannedEntity;

import java.util.Optional;

public interface ObtainAccountBannedPort {
    Optional<AccountBannedEntity> getAccountBanned(Long accountId);
}
