package com.seccreto.infrastructure.transport.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class BufferedReaderOptimized extends Reader implements BufferedReaderInterface {
  private InputStream source;
  private final byte[] buffer;
  private int bufferRead;
  private int bufferSize;
  private boolean sniffing;

  public BufferedReaderOptimized(InputStream source) {
    this.source = source;
    this.buffer = new byte[1024]; 
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
    }

    reset(sniffing);

    // Lê do InputStream
    bufferSize = source.read(buffer);
    if (bufferSize == -1) {
      return -1; // EOF
    }
    bufferRead = 0; // Reseta a leitura do buffer

    // Tente ler novamente após preencher o buffer
    return read(cbuf, off, len);
  }

  public String readLine() throws IOException {
    StringBuilder line = new StringBuilder();
    int ch;
    while ((ch = read()) != -1) { // Use o método read() para acessar o buffer
      if (ch == '\n') {
        break; // Fim da linha
      }
      if (ch != '\r') {
        line.append((char) ch); // Ignora retorno de carro
      }
    }
    return (!line.isEmpty() || ch != -1) ? line.toString() : null; // Retorna null em EOF
  }

  @Override
  public void reset(boolean snif) {
    this.sniffing = snif;
    this.bufferRead = 0;
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