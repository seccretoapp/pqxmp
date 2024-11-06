package com.seccreto.infrastructure.account.presenter;

import com.seccreto.application.account.retrieve.get.AccountOutput;
import com.seccreto.infrastructure.account.models.AccountResponse;

public interface AccountApiPresenter {
  static AccountResponse present(AccountOutput accountOutput) {
    return new AccountResponse(
      accountOutput.id(),
      accountOutput.publicKey().toString(),
      accountOutput.signature(),
      accountOutput.isActive(),
      accountOutput.createdAt(),
      accountOutput.updatedAt(),
      accountOutput.deletedAt()
    );
  }
}
