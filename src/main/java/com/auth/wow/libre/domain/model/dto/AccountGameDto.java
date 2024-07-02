package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountGameDto {
    @NotNull
    @Length(min = 5, max = 40)
    private String username;

    @NotNull
    private String salt;

    @NotNull
    private String verifier;

}
