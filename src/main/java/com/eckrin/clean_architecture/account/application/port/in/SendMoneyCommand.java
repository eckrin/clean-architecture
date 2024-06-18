package com.eckrin.clean_architecture.account.application.port.in;

import com.eckrin.clean_architecture.account.domain.Account;
import com.eckrin.clean_architecture.account.domain.Money;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;

import static com.eckrin.clean_architecture.account.domain.Account.*;
import static com.eckrin.clean_architecture.account.util.ValueUtils.*;

@Getter
public class SendMoneyCommand {

    // value cannot be changed
    @NonNull
    private final AccountId sourceAccountId;
    @NonNull
    private final AccountId targetAccountId;
    @NonNull
    private final Money money;

    public SendMoneyCommand(
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
//        requireNonNull(this);
    }
}
