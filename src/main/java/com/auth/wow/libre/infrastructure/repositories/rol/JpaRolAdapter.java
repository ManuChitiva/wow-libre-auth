package com.auth.wow.libre.infrastructure.repositories.rol;

import com.auth.wow.libre.domain.ports.out.rol.ObtainRolPort;
import com.auth.wow.libre.infrastructure.entities.RolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaRolAdapter implements ObtainRolPort {
    private final RolRepository rolRepository;

    @Autowired
    public JpaRolAdapter(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Optional<RolEntity> findByName(String name, String transactionId) {
        return rolRepository.findByNameAndStatusIsTrue(name);
    }
}
