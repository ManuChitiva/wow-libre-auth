package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.AccountMuted;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountMutedDto {
  @JsonProperty("mute_date")
  private LocalDateTime muteDate;
  @JsonProperty("mute_time")
  private LocalDateTime muteTime;
  @JsonProperty("muted_by")
  private String mutedBy;
  @JsonProperty("reason")
  private String muteReason;

  public AccountMutedDto(AccountMuted accountMuted) {
    this.muteDate = accountMuted.muteDate;
    this.muteTime =  accountMuted.muteTime;
    this.mutedBy = accountMuted.mutedBy;
    this.muteReason = accountMuted.muteReason;
  }
}
