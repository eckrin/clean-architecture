package com.eckrin.clean_architecture.account.adapter.out.persistence;

import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import com.eckrin.clean_architecture.account.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final SpringDataAccountRepository repository;

    @Override
    public void getAccountInfo() {
        repository.findAll();
    }
}
