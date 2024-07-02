package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class AccountWebDto {
    @NotNull
    @Length(min = 2, max = 30)
    private String country;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Length(min = 3, max = 30)
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @Length(min = 3, max = 30)
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("cell_phone")
    private String cellPhone;
    @NotNull
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    private String password;
}
