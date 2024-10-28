package com.seccreto.transport.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MultiplexerConn {
    private final Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public MultiplexerConn(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    /**
     * Reads data from the socket. If sniffing is enabled, you can
     * add additional logic to handle the sniffed data.
     *
     * @param buffer The buffer to store the read data.
     * @return The number of bytes read or -1 if the end of the stream is reached.
     * @throws IOException If an I/O error occurs.
     */
    public int read(byte[] buffer) throws IOException {
        int bytesRead = inputStream.read(buffer);
        // Here you could add logic to process the sniffed data.
        return bytesRead;
    }

    /**
     * Writes data to the socket.
     *
     * @param data The data to be written to the socket.
     * @throws IOException If an I/O error occurs.
     */
    public void write(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }

    /**
     * Closes the underlying socket connection.
     *
     * @throws IOException If an I/O error occurs when closing the socket.
     */
    public void close() throws IOException {
        socket.close();
    }

    /**
     * Gets the underlying Socket object.
     *
     * @return The underlying Socket.
     */
    public Socket getSocket() {
        return socket;
    }
}