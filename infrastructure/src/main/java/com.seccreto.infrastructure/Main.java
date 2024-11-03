package com.seccreto.infrastructure;

import com.seccreto.infrastructure.transport.http.h1.DefaultHttpLayer;
import com.seccreto.infrastructure.transport.http.h2.DefaultHttp2Layer;
import com.seccreto.infrastructure.transport.multiplexer.Multiplexer;
import com.seccreto.infrastructure.transport.multiplexer.MultiplexerListener;
import com.seccreto.infrastructure.transport.multiplexer.BufferedReaderOptimized;
import com.seccreto.infrastructure.transport.multiplexer.Matcher;
import com.seccreto.infrastructure.transport.multiplexer.MatcherInterface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    BufferedReaderOptimized reader = null;

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
      serverSocket = new ServerSocket(8090); // Porta do multiplexador
      Multiplexer multiplexer = new Multiplexer(serverSocket);

      // Adicionando matchers
      MatcherInterface anyMatcher = Matcher.any();
      MatcherInterface http1Matcher = Matcher.http1();
      MatcherInterface http2Matcher = Matcher.http2();
      MatcherInterface http1FastMatcher = Matcher.http1Fast("PATCH", "OPTIONS");
      MatcherInterface tlsMatcher = Matcher.tls(1, 2); // TLS 1.0 e 1.1 como exemplos
      multiplexer.match(httpTransport::matches, http2Transport::matches);

      // Inicializa o listener do multiplexador
      MultiplexerListener listener = multiplexer.match(httpTransport::matches, http2Transport::matches);

      // Inicializa a execução do servidor em uma nova thread
      new Thread(() -> {
        try {
          multiplexer.serve();
        } catch (IOException e) {
          System.err.println("Erro ao iniciar o multiplexador: " + e.getMessage());
        }
      }).start();

      // Inicializa o BufferedReaderOptimized
//      reader = new BufferedReaderOptimized(new BufferedInputStream(new FileInputStream("file1.txt")));

      // Controle de troca de arquivo
      boolean file1Active = true;

      // Loop principal
      while (true) {
        // Monitora eventos e a saúde das conexões
        if (!http2Transport.isHealthy() && httpTransport.isHealthy()) {
          System.out.println("HTTP/2 falhou, revertendo para HTTP/1.1");
        }

        // Tenta aceitar uma nova conexão
        Socket socket = listener.accept();
        if (socket == null) {
          // Nenhuma conexão disponível; espera antes de tentar novamente
          Thread.sleep(1000);
          continue;
        }

        // Usa o Matcher para verificar o protocolo da entrada de dados
        InputStream connectionStream = socket.getInputStream();
        if (http1Matcher.matches(connectionStream)) {
          System.out.println("Conexão identificada como HTTP/1.1");
        } else if (http2Matcher.matches(connectionStream)) {
          System.out.println("Conexão identificada como HTTP/2");
        } else if (http1FastMatcher.matches(connectionStream)) {
          System.out.println("Conexão identificada como HTTP/1.1 com método avançado (PATCH, OPTIONS)");
        } else if (tlsMatcher.matches(connectionStream)) {
          System.out.println("Conexão TLS identificada");
        } else if (anyMatcher.matches(connectionStream)) {
          System.out.println("Conexão corresponde a 'qualquer'");
        } else {
          System.out.println("Protocolo não identificado.");
        }

//         Lê linhas do arquivo usando BufferedReaderOptimized
        String line = reader.readLine();
        if (line != null) {
          System.out.println("Linha lida: " + line);
        } else {
          // Se chegar ao final do arquivo, mude a fonte
          System.out.println("Fim do arquivo. Mudando para o próximo arquivo.");
//          if (file1Active) {
//            reader.setSource(new BufferedInputStream(new FileInputStream("file2.txt")));
//          } else {
//            reader.setSource(new BufferedInputStream(new FileInputStream("file1.txt")));
//          }
//          file1Active = !file1Active;
        }

        // Adicione uma pausa para evitar um loop rápido
        Thread.sleep(1000);
      }

    } catch (IOException | NoSuchAlgorithmException | InterruptedException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      // Fechamento seguro do serverSocket e do reader
      if (serverSocket != null) {
        try {
          serverSocket.close();
        } catch (IOException e) {
          System.err.println("Erro ao fechar o servidor: " + e.getMessage());
        }
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          System.err.println("Erro ao fechar o leitor: " + e.getMessage());
        }
      }
    }
  }
}