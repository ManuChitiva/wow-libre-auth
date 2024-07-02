package com.auth.wow.libre.domain.ports.out.account_muted;

public interface DeleteAccountMutedPort {
  void delete(Long accountId, String transactionId);
}
