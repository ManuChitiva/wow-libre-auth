package com.auth.wow.libre.infrastructure.repositories.account_web;

import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.SaveAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAccountWebAdapter implements SaveAccountWebPort, ObtainAccountWebPort {

    private final AccountWebRepository accountWebRepository;

    public JpaAccountWebAdapter(AccountWebRepository accountWebRepository) {
        this.accountWebRepository = accountWebRepository;
    }

    @Override
    public AccountWebEntity save(AccountWebEntity accountWeb, String transactionId) {
        return accountWebRepository.save(accountWeb);
    }

    @Override
    public Optional<AccountWebEntity> findByEmailAndStatusIsTrue(String email) {
        return accountWebRepository.findByEmailAndStatusIsTrue(email);
    }

}
