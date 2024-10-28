package com.seccreto.infrastructure.transport.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultMultiplexerInterface implements MultiplexerInterface {
    private final ServerSocket serverSocket;
    private final List<MatcherInterface> matcherInterfaces = new ArrayList<>();
    private final ConcurrentLinkedQueue<Socket> socketQueue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private long readTimeout = 0;

    public DefaultMultiplexerInterface(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public MultiplexerListener match(MatcherInterface... matcherInterfaces) {
        for (MatcherInterface matcherInterface : matcherInterfaces) {
            this.matcherInterfaces.add(matcherInterface);
        }
        return new MultiplexerListener(this);
    }

    @Override
    public MultiplexerListener matchWithWriters(MatchWriterInterface... matchers) {
        // Implement similar logic for MatchWriter
        return new MultiplexerListener(this);
    }

    @Override
    public void serve() throws IOException {
        while (!isClosed.get()) {
            Socket socket = serverSocket.accept();
            // Adiciona o socket à fila
            socketQueue.add(socket);
            new Thread(() -> handleConnection(socket)).start();
        }
    }

    private void handleConnection(Socket socket) {
        try {
            InputStream input = socket.getInputStream();
            for (MatcherInterface matcherInterface : matcherInterfaces) {
                if (matcherInterface.matches(input)) {
                    // Aqui você pode manipular a conexão, se necessário
                    return; // Aceita a conexão
                }
            }
            socket.close();
        } catch (IOException e) {
            handleError(e);
        }
    }

    @Override
    public void close() {
        isClosed.set(true);
        try {
            serverSocket.close();
        } catch (IOException e) {
            handleError(e);
        }
    }

    @Override
    public void handleError(Throwable throwable) {
        // Implementação para tratar erros
    }

    @Override
    public void setReadTimeout(long timeout) {
        this.readTimeout = timeout;
    }

    // Método para retornar a lista de matchers
    public List<MatcherInterface> getMatcherInterfaces() {
        return matcherInterfaces; // Retorna a lista de matchers
    }

    // Método para retornar a fila de sockets
    public ConcurrentLinkedQueue<Socket> getSocketQueue() {
        return socketQueue; // Retorna a fila de sockets
    }
}