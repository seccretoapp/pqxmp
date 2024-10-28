package com.seccreto.infrastructure.transport.http.h1;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.util.Headers;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementação padrão da camada HTTP/1.x utilizando Undertow.
 */
public class DefaultHttpLayer implements HttpLayer {
    private Undertow server;
    private String url;
    private AtomicBoolean connected = new AtomicBoolean(false);

    // Variáveis para armazenar dados recebidos e a resposta a ser enviada
    private byte[] receivedData;
    private byte[] dataToSend;

    public DefaultHttpLayer() {
    }

    @Override
    public synchronized void connect(String host, int port, boolean secure) throws Exception {
        if (server != null) {
            throw new IllegalStateException("Servidor já está em execução.");
        }

        Undertow.Builder builder = Undertow.builder();
        if (secure) {
            // Configuração para HTTPS com suporte a TLS
            SSLContext sslContext = createSSLContext("keystore.jks", "247838");
            builder.addHttpsListener(port, host, sslContext);
        } else {
            // Configuração para HTTP sem TLS
            builder.addHttpListener(port, host);
        }

        builder.setServerOption(UndertowOptions.ENABLE_HTTP2, false) // Desabilita HTTP/2
                .setHandler(exchange -> {
                    // Recebe dados da requisição
                    exchange.getRequestReceiver().receiveFullBytes((ex, data) -> {
                        synchronized (this) {
                            this.receivedData = data;
                            System.out.println("Dados recebidos: " + new String(data));

                            // Envia a resposta armazenada
                            if (dataToSend != null) {
                                ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
                                ex.getResponseSender().send(new String(dataToSend));
                            } else {
                                ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                                ex.getResponseSender().send("Hello, HTTP/1.x");
                            }
                        }
                    });
                });

        server = builder.build();
        server.start();

        this.url = (secure ? "https://" : "http://") + host + ":" + port;
        connected.set(true);
        System.out.println("Conexão HTTP/1.x estabelecida com " + this.url);
    }

    @Override
    public synchronized void sendData(byte[] data, Map<String, String> headers) throws Exception {
        if (!connected.get()) {
            throw new IllegalStateException("Servidor não está conectado.");
        }

        // Armazena os dados a serem enviados nas próximas respostas
        this.dataToSend = data;

        // Opcional: Armazena os headers se necessário para personalização
        // Este exemplo não utiliza os headers para simplificação
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, value) -> {
                // Implementar lógica para utilizar os headers conforme necessário
                // Por exemplo, armazenar em uma variável para uso na resposta
                // Não implementado neste exemplo básico
            });
        }

        System.out.println("Dados preparados para envio: " + new String(data));
    }

    @Override
    public synchronized byte[] receiveData() throws Exception {
        if (!connected.get()) {
            throw new IllegalStateException("Servidor não está conectado.");
        }

        if (receivedData == null) {
            throw new IllegalStateException("Nenhum dado recebido ainda.");
        }

        // Retorna os dados recebidos e limpa a variável
        byte[] data = receivedData;
        receivedData = null;
        return data;
    }

    @Override
    public synchronized void close() {
        if (server != null) {
            server.stop();
            server = null;
            connected.set(false);
            System.out.println("Conexão HTTP/1.x fechada.");
        }
    }

    @Override
    public boolean isHealthy() {
        return connected.get();
    }

    /**
     * Cria um contexto SSL a partir de um keystore.
     *
     * @param keystorePath     Caminho para o arquivo keystore.
     * @param keystorePassword Senha do keystore.
     * @return Configuração do contexto SSL.
     * @throws Exception Em caso de erro ao carregar o keystore ou configurar o SSL.
     */
    private SSLContext createSSLContext(String keystorePath, String keystorePassword) throws Exception {
        // Carrega o keystore que contém o certificado
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String requestLine = reader.readLine();
            return requestLine != null && (requestLine.startsWith("GET") || requestLine.startsWith("POST"));
        } catch (IOException e) {
            return false;
        }
    }
}