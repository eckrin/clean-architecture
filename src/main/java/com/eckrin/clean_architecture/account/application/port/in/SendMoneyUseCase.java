package com.eckrin.clean_architecture.account.application.port.in;

public interface SendMoneyUseCase {

    void loadAccount();
    void updateAccountState();
}
