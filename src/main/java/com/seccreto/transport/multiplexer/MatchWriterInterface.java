package com.seccreto.transport.multiplexer;

import java.io.OutputStream;
import java.io.InputStream;

public interface MatchWriterInterface {
    boolean matchesAndWrite(OutputStream output, InputStream input);
}