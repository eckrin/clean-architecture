package com.eckrin.clean_architecture.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.eckrin.clean_architecture.account.AccountTestData.defaultAccount;
import static com.eckrin.clean_architecture.account.ActivityTestData.defaultActivity;
import static com.eckrin.clean_architecture.account.domain.Account.AccountId;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    @DisplayName("단위 테스트 예시 - Account 잔고 감소 테스트 (성공)")
    void withdrawalSuccess() {
        AccountId accountId = new AccountId(1L);
        // Account를 인스턴스화하고
        Account account = defaultAccount()
                .withAccountId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L)).build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L)).build()
                )).build();


        // 생성된 인스턴스를 대상으로 withdraw 실행
        boolean execute = account.withdraw(Money.of(555L), new AccountId(99L));

        assertThat(execute).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L)); // 555+999+1-555 = 1000
    }
}
