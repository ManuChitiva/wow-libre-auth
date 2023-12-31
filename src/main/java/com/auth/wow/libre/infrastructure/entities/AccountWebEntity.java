package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "account_web")
public class AccountWebEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "country")
  private String country;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "cell_phone")
  private String cellPhone;

  @Column(name = "password")
  private String password;

  public AccountWebEntity() {
  }

  public AccountWebEntity(Long id, String country, LocalDate dateOfBirth, String firstName, String lastName, String cellPhone, String password) {
    this.id = id;
    this.country = country;
    this.dateOfBirth = dateOfBirth;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cellPhone = cellPhone;
    this.password = password;
  }

  public static AccountWebEntity fromDomainModel(Account account) {
    return new AccountWebEntity(account.accountWebId, account.country, account.dateOfBirth, account.firstName, account.lastName,
            account.cellPhone, account.password);
  }


}
