package com.seccreto.attachment;

import com.seccreto.Identifier;
import com.seccreto.utils.IdUtils;

import java.util.Objects;

public class AttachmentID extends Identifier {
  private final String value;

  private AttachmentID(final String value) {
    this.value = Objects.requireNonNull(value);
  }

  public static AttachmentID unique() {
    return AttachmentID.from(IdUtils.uuid());
  }

  public static AttachmentID from(final String anId) {
    return new AttachmentID(anId);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final AttachmentID that = (AttachmentID) o;
    return getValue().equals(that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
