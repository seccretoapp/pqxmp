package com.seccreto.infrastructure.configuration.usecases;

import com.seccreto.application.account.retrieve.get.DefaultGetAccountByIdUseCase;
import com.seccreto.application.account.retrieve.get.GetAccountByIdUseCase;
import com.seccreto.infrastructure.account.AccountRedisGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class AccountUseCaseConfig {

  private final AccountRedisGateway accountGateway;

  public AccountUseCaseConfig(AccountRedisGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Bean
  public GetAccountByIdUseCase getAccountByIdUseCase() {
    return new DefaultGetAccountByIdUseCase(accountGateway);
  }
}
