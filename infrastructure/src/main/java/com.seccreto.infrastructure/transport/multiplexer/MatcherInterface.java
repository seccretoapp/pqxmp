package com.seccreto.infrastructure.transport.multiplexer;

import java.io.InputStream;

@FunctionalInterface
public interface MatcherInterface {
  boolean matches(InputStream input);
}