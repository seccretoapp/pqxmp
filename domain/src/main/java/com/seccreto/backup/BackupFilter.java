package com.seccreto.backup;

import com.seccreto.account.AccountID;
import com.seccreto.message.MessageType;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public record BackupFilter(
  Set<MessageType> messageType,
  AccountID accountID,         
  Optional<Instant> startDate,
  Optional<Instant> endDate    
) {}


