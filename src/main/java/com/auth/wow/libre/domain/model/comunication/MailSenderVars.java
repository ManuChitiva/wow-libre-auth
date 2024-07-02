package com.auth.wow.libre.domain.model.comunication;

import lombok.Builder;

@Builder
public class MailSenderVars {
  public final String email;
  public final String username;
  public final String transactionId;
}
