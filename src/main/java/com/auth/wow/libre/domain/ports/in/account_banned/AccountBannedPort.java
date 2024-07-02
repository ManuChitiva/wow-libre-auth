package com.auth.wow.libre.domain.ports.in.account_banned;

import com.auth.wow.libre.domain.model.AccountBanned;

public interface AccountBannedPort {
  AccountBanned getAccountBanned(Long accountId);
}
