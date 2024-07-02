package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.AccountBanned;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AccountBannedDto {
  @JsonProperty("ban_date")
  private Date bandate;
  @JsonProperty("unban_date")
  private Date unbandate;
  @JsonProperty("banned_by")
  private String bannedBy;
  @JsonProperty("reason")
  private String banReason;
  private boolean active;


  public AccountBannedDto(AccountBanned accountBanned) {
    this.bandate = accountBanned.bandate;
    this.unbandate = accountBanned.unbandate;
    this.bannedBy = accountBanned.bannedBy;
    this.banReason = accountBanned.banReason;
    this.active = accountBanned.active;
  }
}
