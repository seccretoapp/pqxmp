package com.seccreto.infrastructure.transport.multiplexer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiplexerListener {
    private final Multiplexer multiplexer;
    private final ConcurrentLinkedQueue<Socket> connectionQueue = new ConcurrentLinkedQueue<>();

    public MultiplexerListener(Multiplexer multiplexer) {
        this.multiplexer = multiplexer;
    }

    /**
     * Accepts a connection if it matches one of the registered matchers.
     *
     * @return The accepted socket, or null if no connections are available.
     * @throws IOException If an I/O error occurs while waiting for a connection.
     */
    public Socket accept() throws IOException {
        Socket socket = multiplexer.getSocketQueue().poll();
        if (socket == null) {
            return null; // Retorna null em vez de lançar uma exceção
        }

        // Check if the connection matches any of the matchers
        for (MatcherInterface matcher : multiplexer.getMatcherInterfaces()) {
            if (matcher.matches(socket.getInputStream())) {
                return socket; // Accept the matched connection
            }
        }

        socket.close(); // Close if not matched
        return null; // Retorna null se nenhum matcher corresponder
    }

    /**
     * A method to add a connection to the queue.
     *
     * @param socket The socket to add.
     */
    public void addConnection(Socket socket) {
        connectionQueue.add(socket);
    }

    /**
     * Closes the listener and all associated connections.
     */
    public void close() {
        for (Socket socket : connectionQueue) {
            try {
                System.out.println("Socket não correspondeu, fechando.");
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket: " + e.getMessage());
            }
        }
    }
}