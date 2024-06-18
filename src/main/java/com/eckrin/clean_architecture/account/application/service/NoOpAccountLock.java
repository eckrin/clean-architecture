package com.eckrin.clean_architecture.account.application.service;

import com.eckrin.clean_architecture.account.application.port.out.AccountLock;
import com.eckrin.clean_architecture.account.domain.Account;
import org.springframework.stereotype.Component;

import static com.eckrin.clean_architecture.account.domain.Account.*;

@Component
class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(AccountId accountId) {
		// do nothing
	}

	@Override
	public void releaseAccount(AccountId accountId) {
		// do nothing
	}

}
