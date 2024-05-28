package com.eckrin.clean_architecture.account.adapter.out.persistence;

import com.eckrin.clean_architecture.account.adapter.out.persistence.entity.AccountEntity;
import com.eckrin.clean_architecture.account.adapter.out.persistence.entity.ActivityEntity;
import com.eckrin.clean_architecture.account.domain.Account;
import com.eckrin.clean_architecture.account.domain.Activity;
import com.eckrin.clean_architecture.account.domain.ActivityWindow;
import com.eckrin.clean_architecture.account.domain.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.eckrin.clean_architecture.account.domain.Account.*;
import static com.eckrin.clean_architecture.account.domain.Activity.*;

@Component
class AccountMapper {

	Account mapToDomainEntity(
			AccountEntity account,
			List<ActivityEntity> activities,
			Long withdrawalBalance,
			Long depositBalance) {

		Money baselineBalance = Money.subtract(
				Money.of(depositBalance),
				Money.of(withdrawalBalance));

		return createAccountWithId(
				new AccountId(account.getId()),
				baselineBalance,
				mapToActivityWindow(activities));

	}

    ActivityWindow mapToActivityWindow(List<ActivityEntity> activities) {
        List<Activity> mappedActivities = new ArrayList<>();

        for (ActivityEntity activity : activities) {
            mappedActivities.add(new Activity(
                    new ActivityId(activity.getId()),
                    new AccountId(activity.getOwnerAccountId()),
                    new AccountId(activity.getSourceAccountId()),
                    new AccountId(activity.getTargetAccountId()),
                    activity.getTimestamp(),
                    Money.of(activity.getAmount())));
        }

        return new ActivityWindow(mappedActivities);
    }

    ActivityEntity mapToJpaEntity(Activity activity) {
        return new ActivityEntity(
                activity.getId() == null ? null : activity.getId().getValue(),
                activity.getOwnerAccountId().getValue(),
                activity.getSourceAccountId().getValue(),
                activity.getTargetAccountId().getValue(),
                activity.getMoney().getAmount().longValue(),
                activity.getTimestamp()
        );
    }

}
