package com.eckrin.clean_architecture.account.application.service;

import com.eckrin.clean_architecture.account.application.port.out.AccountLock;
import com.eckrin.clean_architecture.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.eckrin.clean_architecture.account.domain.Account.*;

@Slf4j
@Component
class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(AccountId accountId) {
		// do nothing
        log.info("Lock Account");
	}

	@Override
	public void releaseAccount(AccountId accountId) {
		// do nothing
        log.info("Release Account");
	}

}
