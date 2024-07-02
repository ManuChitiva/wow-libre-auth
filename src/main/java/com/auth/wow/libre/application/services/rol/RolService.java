package com.auth.wow.libre.application.services.rol;

import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.ports.in.rol.RolPort;
import com.auth.wow.libre.domain.ports.out.rol.ObtainRolPort;
import com.auth.wow.libre.infrastructure.entities.RolEntity;
import org.springframework.stereotype.Service;

@Service

public class RolService implements RolPort {
    private final ObtainRolPort obtainRolPort;

    public RolService(ObtainRolPort obtainRolPort) {
        this.obtainRolPort = obtainRolPort;
    }

    @Override
    public RolModel findByName(String name, String transactionId) {
        return obtainRolPort.findByName(name, transactionId).map(this::mapToModel).orElse(null);
    }

    private RolModel mapToModel(RolEntity rol) {
        return new RolModel(rol.getId(), rol.getName(), rol.isStatus());
    }
}
