package com.seccreto.infrastructure.transport.multiplexer;

import java.io.IOException;

public interface MultiplexerInterface {
    MultiplexerListener match(MatcherInterface... matcherInterfaces);
    MultiplexerListener matchWithWriters(MatcherInterface... matchers);
    void serve() throws IOException;
    void close();
    void handleError(Throwable throwable); // Aceita Throwable
    void setReadTimeout(long timeout);
}