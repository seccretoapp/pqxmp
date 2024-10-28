package com.seccreto.transport.multiplexer;

public interface ErrorHandlerInterface {
    boolean handleError(Throwable throwable);
}