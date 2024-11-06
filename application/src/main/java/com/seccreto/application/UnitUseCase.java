package com.seccreto.application;

import com.seccreto.notification.Notification;
import io.vavr.control.Either;

public abstract class UnitUseCase<IN> {
  public abstract Either<Notification, Object> execute(IN anIN);
}