package com.seccreto.transport.multiplexer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiplexerListener {
    private final DefaultMultiplexerInterface multiplexer;
    private final ConcurrentLinkedQueue<Socket> connectionQueue = new ConcurrentLinkedQueue<>();

    public MultiplexerListener(DefaultMultiplexerInterface multiplexer) {
        this.multiplexer = multiplexer;
    }

    /**
     * Accepts a connection if it matches one of the registered matchers.
     *
     * @return The accepted socket.
     * @throws IOException If an I/O error occurs while waiting for a connection.
     */
    public Socket accept() throws IOException {
        // Wait for a connection from the multiplexer
        Socket socket = multiplexer.getSocketQueue().poll();
        if (socket == null) {
            throw new IOException("No connections available.");
        }

        // Check if the connection matches any of the matchers
        for (MatcherInterface matcher : multiplexer.getMatcherInterfaces()) {
            if (matcher.matches(socket.getInputStream())) {
                return socket; // Accept the matched connection
            }
        }

        socket.close(); // Close if not matched
        throw new IOException("Connection did not match any handlers.");
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
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket: " + e.getMessage());
            }
        }
    }
}