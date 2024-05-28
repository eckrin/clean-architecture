package com.eckrin.clean_architecture.account.application.service;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import com.eckrin.clean_architecture.account.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class SendMoneyService implements SendMoneyUseCase { // incoming port interface

    private final LoadAccountPort outPort; // outgoing port interface
    private final UpdateAccountStatePort updateAccountStatePort; // outgoing port interface - to update account status

    @Override
    @Transactional
    public void loadAccount() {
    }

    @Override
    @Transactional
    public void updateAccountState() {
    }
}
