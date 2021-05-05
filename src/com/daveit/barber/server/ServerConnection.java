package com.daveit.barber.server;

import com.daveit.barber.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection {
    private final ServerSocket serverSocket;

    public ServerConnection(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void acceptClients(ServerController serverController) throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();

            Connection connection = new Connection(socket);
            ClientHandler t = new ClientHandler(connection, serverController);

            Server.clients.put(socket, t);
            t.start();
        }
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }
}
