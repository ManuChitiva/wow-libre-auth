package com.auth.wow.libre.domain.model;

import java.util.Date;

public class AccountBanned {
  public final Long id;
  public final Date bandate;
  public final Date unbandate;
  public final String bannedBy;
  public final String banReason;
  public final boolean active;

  public AccountBanned(Long id, Date bandate, Date unbandate, String bannedBy, String banReason, boolean active) {
    this.id = id;
    this.bandate = bandate;
    this.unbandate = unbandate;
    this.bannedBy = bannedBy;
    this.banReason = banReason;
    this.active = active;
  }
}
