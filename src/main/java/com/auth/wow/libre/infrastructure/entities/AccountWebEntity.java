package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.AccountWebModel;
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

    @Column(name = "email")
    private String email;

    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RolEntity rolId;

    public AccountWebEntity() {
    }

    public AccountWebEntity(Long id, String country, LocalDate dateOfBirth, String firstName, String lastName,
                            String cellPhone, String password, String email) {
        this.id = id;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.password = password;
        this.email = email;
    }

    public AccountWebEntity(String country, LocalDate dateOfBirth, String firstName, String lastName,
                            String cellPhone, String password, String email, RolEntity rolId) {
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.password = password;
        this.email = email;
        this.rolId = rolId;
    }

    public static AccountWebEntity fromDomainModel(AccountWebModel accountWebModel, RolEntity rol) {
        return new AccountWebEntity(accountWebModel.country, accountWebModel.dateOfBirth,
                accountWebModel.firstName,
                accountWebModel.lastName,
                accountWebModel.cellPhone, accountWebModel.password, accountWebModel.email, rol);
    }

    public static AccountWebEntity fromDomainModel(AccountWebModel accountWebModel) {
        return new AccountWebEntity(accountWebModel.id, accountWebModel.country, accountWebModel.dateOfBirth,
                accountWebModel.firstName,
                accountWebModel.lastName,
                accountWebModel.cellPhone, accountWebModel.password, accountWebModel.email);
    }
}
