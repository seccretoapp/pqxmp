package com.seccreto.reaction;

import com.seccreto.account.AccountID;
import com.seccreto.message.MessageID;
import com.seccreto.pagination.Pagination;

public interface ReactionGateway {
  Boolean addReaction(MessageID messageId, AccountID accountID, ReactionType reactionType, Reaction Reaction);
  
  Boolean removeReaction(MessageID messageId, AccountID accountID, ReactionType reactionType, Reaction Reaction);
  
  Pagination<Reaction> getReactions(MessageID messageId);
}
