package com.eckrin.clean_architecture.account.application.service;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyCommand;
import com.eckrin.clean_architecture.account.application.port.out.AccountLock;
import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import com.eckrin.clean_architecture.account.application.port.out.UpdateAccountStatePort;
import com.eckrin.clean_architecture.account.domain.Account;
import com.eckrin.clean_architecture.account.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eckrin.clean_architecture.account.domain.Account.AccountId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

/**
 * Mockito.mock(*.class) 대신 @MockBean 사용한 예시
 * - Junit5에서는 @Runwith가 아닌 @ExtendWith 사용
 * - SendMoneyService에 필요한 moneyTransferProperties는 ReflectionTestUtils를 사용하여 주입
 * - MockitoExtension사용시 UnnecessaryStubbingException 발생 가능 (givenDepositWillSucceed는 givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased()에서 필요하지 않으므로 제거)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SendMoneyServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
	private LoadAccountPort loadAccountPort;

    @MockBean
	private AccountLock accountLock;

    @MockBean
	private UpdateAccountStatePort updateAccountStatePort;

    @Autowired
	private SendMoneyService sendMoneyService;

    @BeforeEach
    public void setup() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // sendMoneyService에 MoneyTransferProperties 주입
        ReflectionTestUtils.setField(sendMoneyService, "moneyTransferProperties", moneyTransferProperties());
    }

	@Test
	void givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {

		AccountId sourceAccountId = new AccountId(41L);
		Account sourceAccount = givenAnAccountWithId(sourceAccountId);

		AccountId targetAccountId = new AccountId(42L);
		givenAnAccountWithId(targetAccountId);

		givenWithdrawalWillFail(sourceAccount);

		SendMoneyCommand command = new SendMoneyCommand(
				sourceAccountId,
				targetAccountId,
				Money.of(300L));

		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isFalse();

		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));
		then(accountLock).should(times(0)).lockAccount(eq(targetAccountId));
	}

	@Test
	void transactionSucceeds() {

		Account sourceAccount = givenSourceAccount();
		Account targetAccount = givenTargetAccount();

		givenWithdrawalWillSucceed(sourceAccount);
		givenDepositWillSucceed(targetAccount);

		Money money = Money.of(500L);

		SendMoneyCommand command = new SendMoneyCommand(
				sourceAccount.getId().get(),
				targetAccount.getId().get(),
				money);

		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isTrue();

		AccountId sourceAccountId = sourceAccount.getId().get();
		AccountId targetAccountId = targetAccount.getId().get();

		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));

		then(accountLock).should().lockAccount(eq(targetAccountId));
		then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(targetAccountId));

		thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
	}

	private void thenAccountsHaveBeenUpdated(AccountId... accountIds){
		ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
		then(updateAccountStatePort).should(times(accountIds.length))
				.updateActivities(accountCaptor.capture());

		List<AccountId> updatedAccountIds = accountCaptor.getAllValues()
				.stream()
				.map(Account::getId)
				.map(Optional::get)
				.collect(Collectors.toList());

		for(AccountId accountId : accountIds){
			assertThat(updatedAccountIds).contains(accountId);
		}
	}

	private void givenDepositWillSucceed(Account account) {
		given(account.deposit(any(Money.class), any(AccountId.class)))
				.willReturn(true);
	}

	private void givenWithdrawalWillFail(Account account) {
		given(account.withdraw(any(Money.class), any(AccountId.class)))
				.willReturn(false);
	}

	private void givenWithdrawalWillSucceed(Account account) {
		given(account.withdraw(any(Money.class), any(AccountId.class)))
				.willReturn(true);
	}

	private Account givenTargetAccount(){
		return givenAnAccountWithId(new AccountId(42L));
	}

	private Account givenSourceAccount(){
		return givenAnAccountWithId(new AccountId(41L));
	}

	private Account givenAnAccountWithId(AccountId id) {
		Account account = Mockito.mock(Account.class);
		given(account.getId())
				.willReturn(Optional.of(id));
		given(loadAccountPort.loadAccount(eq(account.getId().get()), any(LocalDateTime.class)))
				.willReturn(account);
		return account;
	}

	private MoneyTransferProperties moneyTransferProperties(){
		return new MoneyTransferProperties(Money.of(Long.MAX_VALUE));
	}

}
