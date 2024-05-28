package com.eckrin.clean_architecture.account.application.port.out;

import com.eckrin.clean_architecture.account.domain.Account;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account getAccountInfo(Long accountId, LocalDateTime date);
}
