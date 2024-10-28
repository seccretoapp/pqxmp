package com.seccreto.infrastructure.transport.multiplexer;

public interface ErrorHandlerInterface {
    boolean handleError(Throwable throwable);
}