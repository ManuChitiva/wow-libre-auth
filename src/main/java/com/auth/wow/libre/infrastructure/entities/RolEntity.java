package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.RolModel;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "rol")
public class RolEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean status;

    public RolEntity() {
    }

    public RolEntity(Long id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static RolEntity mapToAccountRolEntity(RolModel rol) {
        return new RolEntity(rol.id, rol.name, rol.status);
    }
}
