package com.seccreto.infrastructure.account;

import com.seccreto.account.Account;
import com.seccreto.account.AccountGateway;
import com.seccreto.account.AccountID;
import com.seccreto.infrastructure.account.persistence.AccountRedisEntity;
import com.seccreto.infrastructure.account.persistence.AccountRedisRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AccountRedisGateway implements AccountGateway {

  private final AccountRedisRepository accountRedisRepository;

  public AccountRedisGateway(AccountRedisRepository accountRedisRepository) {
    this.accountRedisRepository = Objects.requireNonNull(accountRedisRepository);
  }

  @Override
  public Optional<Account> getAccount(AccountID accountID) {
    AccountRedisEntity accountEntity = accountRedisRepository.findById(accountID.getValue());
    if (accountEntity == null) {
      return Optional.empty();
    }
    Account account = accountEntity.toAggregate();
    return Optional.of(account);
  }
}
