package com.seccreto.account;

import com.seccreto.AggregateRoot;
import com.seccreto.validation.ValidationHandler;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Account extends AggregateRoot<AccountID> {
  private String username;
  private String publicKey;
  private String signature;
  private Boolean isActive;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Account(
    final AccountID id,
    final String username,
    final String publicKey,
    final String signature,
    final Boolean isActive,
    final Instant createdAt,
    final Instant updatedAt,
    final Instant deletedAt) {
    super(id);
    this.username = username;
    this.publicKey = publicKey;
    this.signature = signature;
    this.isActive = isActive;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static Account with(
    final AccountID id,
    final String username,
    final String publicKey,
    final String signature,
    final Boolean isActive,
    final Instant createdAt,
    final Instant updatedAt,
    final Instant deletedAt
  ) {
    return new Account(
      id,
      username,
      publicKey,
      signature,
      isActive,
      createdAt,
      updatedAt,
      deletedAt
    );
  }

  public static Account with(Account account) {
    return with(
      account.getId(),
      account.getUsername(),
      account.getPublicKey(),
      account.getSignature(),
      account.getIsActive(),
      account.getCreatedAt(),
      account.getUpdatedAt(),
      account.getDeletedAt()
    );
  }


  @Override
  public void validate(ValidationHandler handler) {
    new AccountValidator(this, handler).validate();
  }
}
