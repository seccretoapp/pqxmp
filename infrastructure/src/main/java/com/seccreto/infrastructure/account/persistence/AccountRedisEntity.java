package com.seccreto.infrastructure.account.persistence;

import com.seccreto.account.Account;
import com.seccreto.account.AccountID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Data
@RedisHash("account")
public class AccountRedisEntity implements Serializable {

  @Id
  private String id;
  private String username;
  private String publicKey;
  private String signature;
  private Boolean isActive;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  public AccountRedisEntity() {
  }

  public AccountRedisEntity(
    final String id,
    final String username,
    final String publicKey,
    final String signature,
    final Boolean isActive,
    final Instant createdAt,
    final Instant updatedAt,
    final Instant deletedAt
  ) {
    this.id = id;
    this.username = username;
    this.publicKey = publicKey;
    this.signature = signature;
    this.isActive = isActive;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public AccountRedisEntity from(
    Account account
  ) {
    return new AccountRedisEntity(
      account.getId().getValue(),
      account.getUsername(),
      account.getPublicKey().toString(),
      account.getSignature(),
      account.getIsActive(),
      account.getCreatedAt(),
      account.getUpdatedAt(),
      account.getDeletedAt()
    );
  }

  public Account toAggregate() {
    return Account.with(
      AccountID.from(this.id),
      this.username,
      this.publicKey,
      this.signature,
      this.isActive,
      this.createdAt,
      this.updatedAt,
      this.deletedAt
    );
  }

}
