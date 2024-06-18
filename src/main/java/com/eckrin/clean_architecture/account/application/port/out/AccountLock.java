package com.eckrin.clean_architecture.account.application.port.out;

import com.eckrin.clean_architecture.account.domain.Account;

import static com.eckrin.clean_architecture.account.domain.Account.*;

public interface AccountLock {

	void lockAccount(AccountId accountId);

	void releaseAccount(AccountId accountId);

}