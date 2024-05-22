package com.eckrin.clean_architecture.account.application.port.in;

import jakarta.annotation.Nonnull;
import lombok.Getter;

import static com.eckrin.clean_architecture.account.util.ValueUtils.*;

@Getter
public class SendMoneyCommand {

    // value cannot be changed
    @Nonnull
    private final Long sourceAccountId;
    @Nonnull
    private final Long targetAccountId;
    @Nonnull
    private final Long money;

    public SendMoneyCommand(Long sourceAccountId, Long targetAccountId, Long money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        requireGreaterOrEqualThan(money, 0L);
    }
}
