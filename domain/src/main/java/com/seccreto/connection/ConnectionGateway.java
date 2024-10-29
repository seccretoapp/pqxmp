package com.seccreto.connection;

import com.seccreto.account.AccountID;

import java.time.Instant;
import java.util.Optional;

public interface ConnectionGateway {
  boolean openConnection(String endpoint, AccountID accountId);
  boolean isConnected(AccountID accountId);
  void closeConnection(AccountID accountId);
  boolean sendData(AccountID accountId, Byte[] data);
  Optional<Byte[]> receiveData(AccountID accountId);
  Boolean checkConnectionHealth(AccountID accountId);
  Optional<Instant> getConnectionTime(AccountID accountId);
  Optional<Instant> getLastActivity(AccountID accountId);
  void updateLastActivity(AccountID accountId);
}
