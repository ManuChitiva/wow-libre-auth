package com.auth.wow.libre.infrastructure.repositories.account_muted;

import com.auth.wow.libre.domain.model.AccountMuted;
import com.auth.wow.libre.domain.model.constant.DateHelper;
import com.auth.wow.libre.domain.ports.out.account_muted.DeleteAccountMutedPort;
import com.auth.wow.libre.domain.ports.out.account_muted.ObtainAccountMutedPort;
import com.auth.wow.libre.infrastructure.entities.AccountMutedEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;

@Component
public class JpaAccountMutedAdapter implements ObtainAccountMutedPort, DeleteAccountMutedPort {
  private final AccountMutedRepository accountMutedRepository;

  public JpaAccountMutedAdapter(AccountMutedRepository accountMutedRepository) {
    this.accountMutedRepository = accountMutedRepository;
  }

  @Override
  public AccountMuted getAccountMuted(Long accountId, String transactionId) {
    return accountMutedRepository.findByGuid(accountId).stream().max(Comparator.comparingLong(AccountMutedEntity::getMutedate))
        .map(this::mapToModel).orElse(null);
  }

  private AccountMuted mapToModel(AccountMutedEntity accountMutedEntity) {
    Date dateMuted = Date.from(Instant.ofEpochMilli(accountMutedEntity.getMutedate() * 1000));
    Date desmuteDate = DateHelper.incrementMinutes(dateMuted, accountMutedEntity.getMuteTime().intValue());
    return new AccountMuted(accountMutedEntity.getGuid(),
        DateHelper.getLocalDateTime(dateMuted, "WCO"),
        DateHelper.getLocalDateTime(desmuteDate, "WCO"), accountMutedEntity.getMutedBy(),
        accountMutedEntity.getMuteReason());
  }


  @Override
  public void delete(Long accountId, String transactionId) {
    accountMutedRepository.deleteAll(accountMutedRepository.findByGuid(accountId));
  }
}
