package com.seccreto.exceptions;

import com.seccreto.AggregateRoot;
import com.seccreto.validation.Error;

import java.util.Collections;
import java.util.List;

public class EmailAlreadyRegisterException extends DomainException {

  protected EmailAlreadyRegisterException(final String aMessage, final List<Error> anErrors) {
    super(aMessage, anErrors);
  }

  public static EmailAlreadyRegisterException with(
    final Class<? extends AggregateRoot<?>> anAggregate,
    final String email
  ) {
    final var anError = String.format(
      "Email %s already register in %s"
      , email, anAggregate.getSimpleName());
    return new EmailAlreadyRegisterException(anError, Collections.emptyList());
  }

  public static EmailAlreadyRegisterException with(final Error error) {
    return new EmailAlreadyRegisterException(error.message(), List.of(error));
  }
}
