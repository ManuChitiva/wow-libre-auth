package com.auth.wow.libre.domain.ports.in.account_muted;

import com.auth.wow.libre.domain.model.AccountMuted;

public interface AccountMutedPort {
  AccountMuted getAccountMuted(Long accountId, String transactionId);

}
