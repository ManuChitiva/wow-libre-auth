package com.auth.wow.libre.domain.model.exception;

import org.springframework.http.HttpStatus;

public class InternalException extends GenericErrorException {
  public InternalException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
