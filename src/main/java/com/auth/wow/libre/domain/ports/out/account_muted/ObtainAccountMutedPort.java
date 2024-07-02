package com.auth.wow.libre.domain.ports.out.account_muted;

import com.auth.wow.libre.domain.model.AccountMuted;

public interface ObtainAccountMutedPort {
  AccountMuted getAccountMuted(Long accountId, String transactionId);
}
