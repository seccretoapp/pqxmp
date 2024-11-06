package com.seccreto.attachment;

import java.util.Optional;

public interface AttachmentGateway {
  Attachment saveAttachment(Attachment attachment);

  Optional<Attachment> getAttachment(AttachmentID attachmentId);

  void deleteAttachment(AttachmentID attachmentId);
}
