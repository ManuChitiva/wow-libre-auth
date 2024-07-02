package com.auth.wow.libre.domain.model;

import java.time.LocalDateTime;

public class AccountMuted {
  public final Long id;
  public final LocalDateTime muteDate;
  public final LocalDateTime muteTime;
  public final String mutedBy;
  public final String muteReason;

  public AccountMuted(Long id, LocalDateTime muteDate, LocalDateTime muteTime, String mutedBy, String muteReason) {
    this.id = id;
    this.muteDate = muteDate;
    this.muteTime = muteTime;
    this.mutedBy = mutedBy;
    this.muteReason = muteReason;
  }
}
