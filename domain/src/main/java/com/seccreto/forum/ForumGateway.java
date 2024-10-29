package com.seccreto.forum;

import com.seccreto.message.MessageID;

public interface ForumGateway {
  
  Forum sendMessage(Forum forum);
  
  Boolean voteMessage(MessageID messageId, Boolean upvote);
  
}
