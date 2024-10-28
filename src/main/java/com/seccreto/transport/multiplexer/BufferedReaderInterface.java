package com.seccreto.transport.multiplexer;

import java.io.IOException;
import java.io.InputStream;

public interface BufferedReaderInterface {
    int read(char[] cbuf, int off, int len) throws IOException;
    void reset(boolean snif);
    void close() throws IOException;
    void setSource(InputStream source);
}