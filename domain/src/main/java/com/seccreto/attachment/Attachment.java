package com.seccreto.attachment;

import com.seccreto.AggregateRoot;
import com.seccreto.validation.ValidationHandler;

import java.time.Instant;

public class Attachment extends AggregateRoot<AttachmentID> {
  
  private String name;
  private String url;
  private Long size;
  private String contentType;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  protected Attachment(AttachmentID attachmentID) {
    super(attachmentID);
  }

  @Override
  public void validate(ValidationHandler handler) {
    
  }
}
