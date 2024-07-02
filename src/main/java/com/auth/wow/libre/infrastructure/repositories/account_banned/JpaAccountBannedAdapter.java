package com.auth.wow.libre.infrastructure.repositories.account_banned;

import com.auth.wow.libre.domain.ports.out.account_banned.ObtainAccountBannedPort;
import com.auth.wow.libre.infrastructure.entities.AccountBannedEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAccountBannedAdapter implements ObtainAccountBannedPort {

  private final AccountBannedRepository accountBannedRepository;

  public JpaAccountBannedAdapter(AccountBannedRepository accountBannedRepository) {
    this.accountBannedRepository = accountBannedRepository;
  }

  @Override
  public Optional<AccountBannedEntity> getAccountBanned(Long accountId) {
    return accountBannedRepository.findByAccountIdAndActiveIsTrue(accountId);
  }

}
