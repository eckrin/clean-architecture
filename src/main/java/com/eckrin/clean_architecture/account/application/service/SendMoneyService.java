package com.eckrin.clean_architecture.account.application.service;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyCommand;
import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import com.eckrin.clean_architecture.account.application.port.out.AccountLock;
import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import com.eckrin.clean_architecture.account.application.port.out.UpdateAccountStatePort;
import com.eckrin.clean_architecture.account.application.service.exception.ThresholdExceededException;
import com.eckrin.clean_architecture.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.eckrin.clean_architecture.account.domain.Account.*;

@Service
@RequiredArgsConstructor
class SendMoneyService implements SendMoneyUseCase { // incoming port interface

    private final LoadAccountPort outPort; // outgoing port interface
    private final UpdateAccountStatePort updateAccountStatePort; // outgoing port interface - to update account status

    private final AccountLock accountLock;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    @Transactional
    public boolean sendMoney(SendMoneyCommand command) {

        checkThreshold(command);

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        Account sourceAccount = outPort.loadAccount(
                command.getSourceAccountId(),
                baselineDate);

        Account targetAccount = outPort.loadAccount(
                command.getTargetAccountId(),
                baselineDate);

        AccountId sourceAccountId = sourceAccount.getId()
                .orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
        AccountId targetAccountId = targetAccount.getId()
                .orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

        accountLock.lockAccount(sourceAccountId);
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }

        accountLock.lockAccount(targetAccountId);
        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);
        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if(command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())){
            throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }
}
