package com.seccreto.transport.http;

import com.seccreto.transport.http.h2.DefaultHttp2Layer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultHttp2LayerTest {

    private DefaultHttp2Layer http2Layer;

    @BeforeEach
    void setUp() throws Exception {
        http2Layer = new DefaultHttp2Layer();
        http2Layer.connect("localhost", 8080, false);
    }

    @AfterEach
    void tearDown() throws Exception {
        http2Layer.close();
    }

    @Test
    void testConnect() {
        assertTrue(http2Layer.isHealthy(), "A conexão deve estar saudável após conectar.");
    }

    @Test
    void testSendData() throws Exception {
        byte[] data = "Hello, HTTP/2".getBytes();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");

        assertDoesNotThrow(() -> http2Layer.sendData(data, headers));
    }

    @Test
    void testReceiveData() throws Exception {
        byte[] responseData = http2Layer.receiveData();
        assertNotNull(responseData);
        assertEquals("Response from server", new String(responseData));
    }

    @Test
    void testClose() {
        assertTrue(http2Layer.isHealthy(), "A conexão deve estar saudável após conectar.");
        assertDoesNotThrow(() -> http2Layer.close());
        assertFalse(http2Layer.isHealthy(), "A conexão deve estar fechada após o fechamento.");
    }
}