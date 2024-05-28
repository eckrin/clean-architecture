package com.eckrin.clean_architecture.account.application.port.out;

import com.eckrin.clean_architecture.account.domain.Account;

public interface UpdateAccountStatePort {
    void updateActivities(Account account);
}
