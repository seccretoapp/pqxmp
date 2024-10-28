package com.seccreto;

import com.seccreto.transport.http.h1.DefaultHttpLayer;
import com.seccreto.transport.http.h2.DefaultHttp2Layer;
import com.seccreto.transport.multiplexer.DefaultMultiplexerInterface;
import com.seccreto.transport.multiplexer.MultiplexerListener;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        try {
            // Configurações gerais do protocolo
            String host = "localhost";
            int httpPort = 8070;
            int http2Port = 8080;

            // Inicialização das camadas de transporte
            DefaultHttpLayer httpTransport = new DefaultHttpLayer();
            DefaultHttp2Layer http2Transport = new DefaultHttp2Layer();

            // Inicializa HTTP/1.1
            System.out.println("Inicializando transporte HTTP/1.1...");
            httpTransport.connect(host, httpPort, false);
            if (httpTransport.isHealthy()) {
                System.out.println("Transporte HTTP/1.1 iniciado com sucesso!");
            }

            // Inicializa a camada HTTP/2
            System.out.println("Iniciando transporte HTTP/2...");
            http2Transport.connect(host, http2Port, false);
            if (http2Transport.isHealthy()) {
                System.out.println("Transporte HTTP/2 iniciado com sucesso!");
            }

            // Inicializa o servidor multiplexador
            ServerSocket serverSocket = new ServerSocket(8090); // Porta do multiplexador
            DefaultMultiplexerInterface multiplexer = new DefaultMultiplexerInterface(serverSocket);

            // Adicionando matchers
            multiplexer.match(httpTransport::matches, http2Transport::matches);

            // Inicializa o listener do multiplexador
            MultiplexerListener listener = multiplexer.match(httpTransport::matches, http2Transport::matches);

            // Inicializa a execução do servidor
            new Thread(() -> {
                try {
                    multiplexer.serve();
                } catch (IOException e) {
                    System.err.println("Erro ao iniciar o multiplexador: " + e.getMessage());
                }
            }).start();

            // Loop principal
            while (true) {
                // Monitora eventos e a saúde das conexões
                if (!http2Transport.isHealthy() && httpTransport.isHealthy()) {
                    System.out.println("HTTP/2 falhou, revertendo para HTTP/1.1");
                    // Aqui você poderia implementar lógica para mudar o transporte, se necessário
                }
                // processar eventos se necessário
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}