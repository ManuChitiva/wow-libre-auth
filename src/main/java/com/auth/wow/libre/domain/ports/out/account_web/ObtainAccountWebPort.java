package com.auth.wow.libre.domain.ports.out.account_web;

import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

import java.util.Optional;

public interface ObtainAccountWebPort {
    Optional<AccountWebEntity> findByEmailAndStatusIsTrue(String email);
}
