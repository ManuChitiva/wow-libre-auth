package com.auth.wow.libre.infrastructure.repositories.account_muted;

import com.auth.wow.libre.infrastructure.entities.AccountMutedEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountMutedRepository extends CrudRepository<AccountMutedEntity, Long> {
  List<AccountMutedEntity> findByGuid(Long guid);

}
