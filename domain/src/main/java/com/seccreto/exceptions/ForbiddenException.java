package com.seccreto.exceptions;

import com.seccreto.AggregateRoot;
import com.seccreto.validation.Error;

import java.util.Collections;
import java.util.List;

public class ForbiddenException extends DomainException {

  protected ForbiddenException(final String aMessage, final List<Error> anErrors) {
    super(aMessage, anErrors);
  }

  public static ForbiddenException with(
    final Class<? extends AggregateRoot<?>> anAggregate,
    final String id
  ) {
    final var anError = "%s ID is not allowed to perform this action".formatted(
      anAggregate.getSimpleName(),
      id
    );
    return new ForbiddenException(anError, Collections.emptyList());
  }

  public static ForbiddenException with(final Error error) {
    return new ForbiddenException(error.message(), List.of(error));
  }
}