package com.seccreto.account;

import com.seccreto.validation.Error;
import com.seccreto.validation.ValidationHandler;
import com.seccreto.validation.Validator;

public class AccountValidator extends Validator {

  private static final int MAX_USERNAME_LENGTH = 100;

  private final Account account;

  public AccountValidator(Account account, ValidationHandler handler) {
    super(handler);
    this.account = account;
  }

  @Override
  public void validate() {
    validateUsername();
  }

  private void validateUsername() {
    String username = account.getUsername();
    if (username != null && username.length() > MAX_USERNAME_LENGTH) {
      validationHandler().append(new Error("Username exceeds maximum length"));
    }
  }
}
