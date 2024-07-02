package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.infrastructure.entities.dto.AccountMutedId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
@IdClass(AccountMutedId.class)

@Data
@Entity
@Table(name = "account_muted")
public class AccountMutedEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long guid;
  @Column(name = "mutedate")
  private Long mutedate;
  @Column(name = "mutetime")
  private Long muteTime;
  @Column(name = "mutedby")
  private String mutedBy;
  @Column(name = "mutereason")
  private String muteReason;


}
