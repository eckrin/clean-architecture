package com.eckrin.clean_architecture.account.application.port.out;

import com.eckrin.clean_architecture.account.domain.Account;

import java.time.LocalDateTime;

import static com.eckrin.clean_architecture.account.domain.Account.*;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
