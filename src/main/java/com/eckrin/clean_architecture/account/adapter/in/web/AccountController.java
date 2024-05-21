package com.eckrin.clean_architecture.account.adapter.in.web;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @GetMapping("/test")
    public String test() {

        sendMoneyUseCase.loadAccount();

        return "success";
    }
}
