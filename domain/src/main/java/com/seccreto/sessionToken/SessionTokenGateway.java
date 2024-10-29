package com.seccreto.sessionToken;

import java.util.Optional;

public interface SessionTokenGateway {
  SessionToken createSessionToken(SessionToken sessionToken);
  Boolean validateSessionToken(SessionTokenID sessionTokenID);
  Boolean expireSessionToken(SessionTokenID sessionTokenID);
  Optional<SessionToken> getSessionTokenData(SessionTokenID sessionTokenID);
}