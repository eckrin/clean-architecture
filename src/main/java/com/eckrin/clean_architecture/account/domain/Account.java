package com.eckrin.clean_architecture.account.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

/**
 * 최대한 불변성을 유지
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    private final AccountId id;
    private final ActivityWindow activityWindow;
    private final Money balance;

    public static Account createAccountWithoutId(Money balance, ActivityWindow activityWindow) {
        return new Account(null, activityWindow, balance);
    }

    public static Account createAccountWithId(AccountId id, Money balance, ActivityWindow activityWindow) {
        return new Account(id, activityWindow, balance);
    }

    @Value
    public static class AccountId {
        private Long value;
    }
}
