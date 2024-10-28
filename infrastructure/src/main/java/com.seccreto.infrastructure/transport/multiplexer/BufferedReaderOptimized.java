package com.seccreto.infrastructure.transport.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class BufferedReaderOptimized extends Reader implements BufferedReaderInterface {
    private InputStream source;
    private byte[] buffer;
    private int bufferRead;
    private int bufferSize;
    private boolean sniffing;

    public BufferedReaderOptimized(InputStream source) {
        this.source = source;
        this.buffer = new byte[0];
        this.bufferRead = 0;
        this.bufferSize = 0;
        this.sniffing = false;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (bufferSize > bufferRead) {
            int bytesToCopy = Math.min(len, bufferSize - bufferRead);
            System.arraycopy(buffer, bufferRead, cbuf, off, bytesToCopy);
            bufferRead += bytesToCopy;
            return bytesToCopy;
        } else if (!sniffing && buffer.length != 0) {
            buffer = new byte[0]; // Clear buffer if not sniffing
        }

        byte[] tempBuffer = new byte[len];
        int bytesRead = source.read(tempBuffer, 0, len);
        if (bytesRead > 0 && sniffing) {
            byte[] newBuffer = new byte[buffer.length + bytesRead];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            System.arraycopy(tempBuffer, 0, newBuffer, buffer.length, bytesRead);
            buffer = newBuffer;
        }

        return bytesRead;
    }

    public String readLine() throws IOException {
        StringBuilder line = new StringBuilder();
        int ch;
        while ((ch = source.read()) != -1) {
            if (ch == '\n') {
                break; // End of line
            }
            if (ch != '\r') {
                line.append((char) ch); // Skip carriage return
            }
        }
        return (line.length() > 0 || ch != -1) ? line.toString() : null; // Return null on EOF
    }

    @Override
    public void reset(boolean snif) {
        this.sniffing = snif;
        this.bufferRead = Integer.parseInt(null);
        this.bufferSize = buffer.length;
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public void setSource(InputStream source) {
        this.source = source;
    }
}