package com.seccreto.live;

import com.seccreto.account.AccountID;
import com.seccreto.message.MessageID;

public interface LiveGateway {
  Live startSession(Live live);
  void endSession(LiveID liveID);
  Boolean sendMessage(LiveID liveID, AccountID senderId, MessageID message);
}
