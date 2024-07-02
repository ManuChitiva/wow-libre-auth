package com.auth.wow.libre.infrastructure.repositories.account;

import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.domain.ports.out.account.SaveAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JpaAccountAdapter implements SaveAccountPort, ObtainAccountPort {

    private final AccountRepository accountRepository;

    public JpaAccountAdapter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void save(AccountEntity account) {
        accountRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<AccountEntity> findByAccountWebId(Long id) {
        return accountRepository.findByAccountWebId(id);
    }

    @Override
    public Optional<AccountEntity> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<AccountEntity> findByIdAndAccountWeb(Long id, Long accountWebId, String transactionId) {
        return accountRepository.findByIdAndAccountWebId(id, accountWebId);
    }
}
