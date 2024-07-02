package com.auth.wow.libre.infrastructure.entities.dto;

import java.io.Serializable;
import java.util.Objects;

public class AccountMutedId implements Serializable {
  private Long guid;
  private Long mutedate;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccountMutedId that = (AccountMutedId) o;
    return Objects.equals(guid, that.guid) &&
           Objects.equals(mutedate, that.mutedate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guid, mutedate);
  }
}
