package com.seccreto.account;

import com.seccreto.Identifier;
import com.seccreto.utils.IdUtils;

import java.util.Objects;

public class AccountID extends Identifier {
  private final String value;

  private AccountID(final String value) {
    this.value = Objects.requireNonNull(value);
  }

  public static AccountID unique() {
    return AccountID.from(IdUtils.uuid());
  }

  public static AccountID from(final String anId) {
    return new AccountID(anId);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final AccountID that = (AccountID) o;
    return getValue().equals(that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
