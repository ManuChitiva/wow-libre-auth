package com.auth.wow.libre.infrastructure.repositories.account_web;

import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountWebRepository extends CrudRepository<AccountWebEntity, Long> {
  Optional<AccountWebEntity> findByEmailAndStatusIsTrue(final String email);
}
