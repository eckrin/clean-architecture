package com.eckrin.clean_architecture.account.adapter.out.persistence;

import com.eckrin.clean_architecture.account.adapter.out.persistence.entity.AccountEntity;
import com.eckrin.clean_architecture.account.adapter.out.persistence.entity.ActivityEntity;
import com.eckrin.clean_architecture.account.application.port.out.LoadAccountPort;
import com.eckrin.clean_architecture.account.application.port.out.UpdateAccountStatePort;
import com.eckrin.clean_architecture.account.domain.Account;
import com.eckrin.clean_architecture.account.domain.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.eckrin.clean_architecture.account.domain.Account.*;

/**
 * 영속성 어댑터 - 애플리케이션의 아웃고잉 포트를 구현
 */
@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final SpringDataAccountRepository accountRepository;
    private final SpringDataActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(
            AccountId accountId,
            LocalDateTime baselineDate) {

        AccountEntity account =
                accountRepository.findById(accountId.getValue())
                        .orElseThrow(RuntimeException::new);

        List<ActivityEntity> activities =
                activityRepository.findByOwnerSince(
                        accountId.getValue(),
                        baselineDate);

        Long withdrawalBalance = orZero(activityRepository
                .getWithdrawalBalanceUntil(
                        accountId.getValue(),
                        baselineDate));

        Long depositBalance = orZero(activityRepository
                .getDepositBalanceUntil(
                        accountId.getValue(),
                        baselineDate));

        return accountMapper.mapToDomainEntity(
                account,
                activities,
                withdrawalBalance,
                depositBalance);

    }

    @Override
    public void updateActivities(Account account) {
        for (Activity activity : account.getActivityWindow().getActivities()) {
            if (activity.getId() == null) {
                activityRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }

    private Long orZero(Long value) {
        return value==null?0L:value;
    }

}
