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

/**
 * 영속성 어댑터 - 애플리케이션의 아웃고잉 포트를 구현
 */
@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final SpringDataAccountRepository accountRepository;
    private final SpringDataActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    /**
     * 계좌 정보와, 해당 계좌의 특정 기간동안 활동 불러오기
     */
    @Override
    public Account getAccountInfo(Long accountId, LocalDateTime date) {
        AccountEntity account = accountRepository.findById(accountId).orElseThrow(()->new RuntimeException("AccountEntity Not Found"));
        List<ActivityEntity> activities = activityRepository.findByOwnerSince(accountId, date);
        Long withdrawalBalance = orZero(activityRepository.getWithdrawalBalanceUntil(accountId, date));
        Long depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId, date));

        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance);
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
