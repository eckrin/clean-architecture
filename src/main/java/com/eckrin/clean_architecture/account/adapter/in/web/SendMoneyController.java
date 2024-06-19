package com.eckrin.clean_architecture.account.adapter.in.web;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyCommand;
import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import com.eckrin.clean_architecture.account.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.eckrin.clean_architecture.account.domain.Account.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping(path = "/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccountId,
            @PathVariable("targetAccountId") Long targetAccountId,
            @PathVariable("amount") Long amount) {

        SendMoneyCommand command = new SendMoneyCommand(
                new AccountId(sourceAccountId),
                new AccountId(targetAccountId),
                Money.of(amount));

        sendMoneyUseCase.sendMoney(command);
    }
}
