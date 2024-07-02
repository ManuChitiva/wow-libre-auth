package com.auth.wow.libre.domain.ports.out.rol;

import com.auth.wow.libre.infrastructure.entities.RolEntity;

import java.util.Optional;

public interface ObtainRolPort {
    Optional<RolEntity> findByName(String name, String transactionId);
}
