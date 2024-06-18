package com.eckrin.clean_architecture.account.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 도메인 엔티티는 다양한 상태 변경 메서드를 가질 수 있다 (풍부한 도메인 모델 채용)
 * 최대한 불변성을 유지
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    private final AccountId id;
    private final ActivityWindow activityWindow;
    private final Money balance;

    public Optional<AccountId> getId(){
        return Optional.ofNullable(this.id);
    }

    public static Account createAccountWithoutId(Money balance, ActivityWindow activityWindow) {
        return new Account(null, activityWindow, balance);
    }

    public static Account createAccountWithId(AccountId id, Money balance, ActivityWindow activityWindow) {
        return new Account(id, activityWindow, balance);
    }

    public static Account withId(
            AccountId accountId,
            Money baselineBalance,
            ActivityWindow activityWindow) {
        return new Account(accountId, activityWindow, baselineBalance);
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {

        if (!mayWithdraw(money)) {
            return false;
        }

        Activity withdrawal = new Activity(
                this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                money);
        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return Money.add(
                        this.calculateBalance(),
                        money.negate())
                .isPositiveOrZero();
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = new Activity(
                this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                money);
        this.activityWindow.addActivity(deposit);
        return true;
    }

    public Money calculateBalance() {
        return Money.add(
                this.balance,
                this.activityWindow.calculateBalance(this.id));
    }

    @Value
    public static class AccountId {
        private Long value;
    }
}
