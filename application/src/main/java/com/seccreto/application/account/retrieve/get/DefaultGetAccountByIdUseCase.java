package com.seccreto.application.account.retrieve.get;

import com.seccreto.account.Account;
import com.seccreto.account.AccountGateway;
import com.seccreto.account.AccountID;
import com.seccreto.exceptions.DomainException;
import com.seccreto.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAccountByIdUseCase extends GetAccountByIdUseCase {

  private final AccountGateway accountGateway;

  public DefaultGetAccountByIdUseCase(AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public AccountOutput execute(final String anIN) {
    final var anAccountId = AccountID.from(anIN);
    return this.accountGateway.getAccount(anAccountId).map(AccountOutput::from).orElseThrow(notFound(anAccountId));
  }

  private Supplier<NotFoundException> notFound(final AccountID anId) {
    return () -> NotFoundException.with(Account.class, anId);
  }
}
