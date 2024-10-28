package com.seccreto.infrastructure.transport.multiplexer;

import java.io.InputStream;
import java.io.OutputStream;

public interface MatchWriterInterface {
    boolean matchesAndWrite(OutputStream output, InputStream input);
}