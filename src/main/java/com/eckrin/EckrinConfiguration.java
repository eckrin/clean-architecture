package com.eckrin;

import com.eckrin.clean_architecture.account.application.service.MoneyTransferProperties;
import com.eckrin.clean_architecture.account.domain.Money;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EckrinConfigurationProperties.class)
public class EckrinConfiguration {

  @Bean
  public MoneyTransferProperties moneyTransferProperties(EckrinConfigurationProperties eckrinConfigurationProperties){
    return new MoneyTransferProperties(Money.of(eckrinConfigurationProperties.getTransferThreshold()));
  }

}
