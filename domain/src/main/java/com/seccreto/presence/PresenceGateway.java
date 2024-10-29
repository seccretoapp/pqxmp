package com.seccreto.presence;

import com.seccreto.account.AccountID;

import java.util.Optional;

public interface PresenceGateway {
  Boolean setPresence(Presence presence);
  Optional<Presence> getPresence(AccountID accountID);
}
