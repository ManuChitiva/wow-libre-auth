package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountChangePasswordDto {
    @NotNull
    private String salt;
    @NotNull
    private String verifier;
    @NotNull
    @JsonProperty("account_id")
    private Long accountId;
    @NotNull
    private String password;
}
