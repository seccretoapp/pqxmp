package com.seccreto.message;

import com.seccreto.attachment.Attachment;
import com.seccreto.attachment.AttachmentID;
import com.seccreto.pagination.Pagination;
import com.seccreto.pagination.SearchQuery;
import com.seccreto.role.Role;

import java.util.Optional;

public interface MessageGateway {
  Message create(Message message);
  Optional<Boolean> markAsRead(MessageID id);
  Optional<Boolean> markAsUnread(MessageID id);
  Pagination<Message> readAll(SearchQuery searchQuery);
  
  Message update(Message message);
  void delete(Role role, MessageID id);

  void addAttachment(MessageID messageId, Attachment attachment);
  Attachment getAttachment(MessageID messageId, AttachmentID attachmentId);
  
  void notify(MessageID messageId, String eventType);
}
