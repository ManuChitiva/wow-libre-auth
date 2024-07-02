package com.auth.wow.libre.infrastructure.repositories.account;

import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);

    List<AccountEntity> findByAccountWebId(Long id);

    Optional<AccountEntity> findByIdAndAccountWebId(Long id, Long accountWebId);
}
