package com.seccreto.security;

import com.seccreto.account.AccountID;

import java.time.Instant;

public interface SecurityGateway {
  boolean validateSignature(Instant data, String signature, String publicKey);
  boolean authorize(AccountID accountID, ActionType action);
}
