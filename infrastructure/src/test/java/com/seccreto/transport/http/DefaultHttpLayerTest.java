package com.seccreto.transport.http;

import com.seccreto.infrastructure.transport.http.h1.DefaultHttpLayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para DefaultHttpLayer.
 */
class DefaultHttpLayerTest {

    private DefaultHttpLayer httpLayer;
    private final String HOST = "localhost";
    private final int PORT = 8080;
    private final boolean SECURE = false; // Defina como true se quiser testar HTTPS

    @BeforeEach
    void setUp() throws Exception {
        httpLayer = new DefaultHttpLayer();
        httpLayer.connect(HOST, PORT, SECURE);
    }

    @AfterEach
    void tearDown() throws Exception {
        httpLayer.close();
    }

    @Test
    void testConnect() {
        assertTrue(httpLayer.isHealthy(), "A conexão deve estar saudável após conectar.");
    }

    @Test
    void testSendData() throws Exception {
        byte[] data = "Dados de teste para envio".getBytes();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");

        assertDoesNotThrow(() -> httpLayer.sendData(data, headers),
                "Enviar dados não deve lançar exceções.");
    }

    @Test
    void testReceiveData() throws Exception {
        // Dados a serem enviados pelo cliente
        String requestData = "Dados de teste do cliente";
        byte[] requestBytes = requestData.getBytes();

        // Envia uma requisição POST ao servidor
        URL url = new URL((SECURE ? "https" : "http") + "://" + HOST + ":" + PORT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/plain");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBytes);
            os.flush();
        }

        // Lê a resposta do servidor
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "O código de resposta deve ser 200 OK.");

        // Verifica a resposta enviada pelo servidor
        byte[] responseData = httpLayer.receiveData();
        assertNotNull(responseData, "Os dados recebidos não devem ser nulos.");
        String responseString = new String(responseData);
        assertEquals(requestData, responseString, "Os dados recebidos devem corresponder aos dados enviados.");
    }

    @Test
    void testClose() {
        assertTrue(httpLayer.isHealthy(), "A conexão deve estar saudável após conectar.");
        assertDoesNotThrow(() -> httpLayer.close(), "Fechar a conexão não deve lançar exceções.");
        assertFalse(httpLayer.isHealthy(), "A conexão deve estar fechada após o fechamento.");
    }
}