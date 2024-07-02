package com.auth.wow.libre.domain.ports.in.rol;

import com.auth.wow.libre.domain.model.RolModel;

public interface RolPort {
    RolModel findByName(String name, String transactionId);
}
