package com.seccreto.application.account.retrieve.get;

import com.seccreto.account.Account;

import java.time.Instant;

public record AccountOutput(
  String id,
  String username,
  String publicKey,
  String signature,
  Boolean isActive,
  Instant createdAt,
  Instant updatedAt,
  Instant deletedAt
) {
  public static AccountOutput from(
    final Account account
  ) {
    return new AccountOutput(
      account.getId().getValue(),
      account.getUsername(),
      account.getPublicKey(),
      account.getSignature(),
      account.getIsActive(),
      account.getCreatedAt(),
      account.getUpdatedAt(),
      account.getDeletedAt()
    );
  }
}
