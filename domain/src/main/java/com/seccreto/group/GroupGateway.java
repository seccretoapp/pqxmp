package com.seccreto.group;

import com.seccreto.account.AccountID;
import com.seccreto.message.MessageID;

public interface GroupGateway {
  Boolean sendMessageToGroup(Group groupId, AccountID senderId, MessageID message);
}
