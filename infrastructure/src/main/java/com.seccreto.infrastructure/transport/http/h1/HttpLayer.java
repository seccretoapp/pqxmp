package com.seccreto.infrastructure.transport.http.h1;

import java.io.InputStream;
import java.util.Map;

public interface HttpLayer {
    void connect(String host, int port, boolean secure) throws Exception;
    void sendData(byte[] data, Map<String, String> headers) throws Exception;
    byte[] receiveData() throws Exception;
    void close() throws Exception;
    boolean isHealthy();
    boolean matches(InputStream stream);
}