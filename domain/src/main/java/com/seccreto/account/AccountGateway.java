package com.seccreto.account;

import java.util.Optional;

public interface AccountGateway {
  Optional<Account> getAccount(AccountID accountID);
}
