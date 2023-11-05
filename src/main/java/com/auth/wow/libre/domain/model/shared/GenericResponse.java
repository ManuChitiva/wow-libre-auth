package com.auth.wow.libre.domain.model.shared;

import lombok.Data;

@Data
public class GenericResponse<D> {
    private int code;
    private String message;
    private String transactionId;
    private D data;
}
