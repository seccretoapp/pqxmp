package com.seccreto.transport.http.h2;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.util.Headers;
import org.xnio.Options;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class DefaultHttp2Layer implements Http2Layer {
    private Undertow server;
    private String url;
    private AtomicBoolean connected = new AtomicBoolean(false);

    public DefaultHttp2Layer() {
    }

    @Override
    public void connect(String host, int port, boolean secure) throws Exception {
        if (server != null) {
            throw new IllegalStateException("Server already running.");
        }

        Undertow.Builder builder = Undertow.builder();
        if (secure) {
            // Configuração para HTTPS/2 com suporte a TLS
            SSLContext sslContext = createSSLContext("keystore.jks", "247838");
            builder.addHttpsListener(port, host, sslContext);
        } else {
            // Configuração para HTTP/2 sem TLS
            builder.addHttpListener(port, host);
        }

        builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello, HTTP/2");
                });

        server = builder.build();
        server.start();

        this.url = (secure ? "https://" : "http://") + host + ":" + port;
        connected.set(true);
        System.out.println("Conexão HTTP/2 estabelecida com " + this.url);
    }

    @Override
    public void sendData(byte[] data, Map<String, String> headers) throws Exception {
        if (!connected.get()) {
            throw new IllegalStateException("Não conectado.");
        }

        // Configuração básica para enviar dados utilizando Undertow é necessária
        // Se você estiver utilizando clientes para enviar a requisição HTTP/2,
        // essa parte precisará de configuração adicional.
        System.out.println("Dados enviados: " + new String(data));
    }

    @Override
    public byte[] receiveData() throws Exception {
        if (!connected.get()) {
            throw new IllegalStateException("Não conectado.");
        }

        // Para receber dados, precisamos configurar uma resposta adequada usando Undertow
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Response from server".getBytes());
        return outputStream.toByteArray();
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop();
            connected.set(false);
            System.out.println("Conexão HTTP/2 fechada.");
        }
    }

    @Override
    public boolean isHealthy() {
        return connected.get();
    }

    private SSLContext createSSLContext(String keystorePath, String keystorePassword) throws Exception {
        // Carregar o keystore que contém o certificado
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        // Configuração do KeyManager para gerenciar o SSL
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        // Configuração do contexto SSL
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        return sslContext;
    }

    @Override
    public boolean matches(InputStream input) {
        byte[] preface = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes();
        byte[] buffer = new byte[preface.length];
        try {
            int bytesRead = input.read(buffer);
            return bytesRead >= preface.length && new String(buffer).equals(new String(preface));
        } catch (IOException e) {
            return false;
        }
    }
}