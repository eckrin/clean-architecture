package com.eckrin.clean_architecture.account.application;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort outPort;

    @Override
    public void loadAccount() {
        outPort.getAccountInfo();
    }

    @Override
    public void updateAccountState() {

    }
}
