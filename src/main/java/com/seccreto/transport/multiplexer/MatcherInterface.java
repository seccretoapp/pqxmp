package com.seccreto.transport.multiplexer;

import java.io.InputStream;

public interface MatcherInterface {
    boolean matches(InputStream input);
}