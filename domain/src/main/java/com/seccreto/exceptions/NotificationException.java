package com.seccreto.exceptions;

import com.seccreto.validation.handler.Notification;

public class NotificationException extends DomainException {

  public NotificationException(final String aMessage, final Notification notification) {
    super(aMessage, notification.getErrors());
  }
}
