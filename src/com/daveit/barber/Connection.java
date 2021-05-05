package com.daveit.barber;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
    final public Socket socket;
    InetAddress ip = null;

    final public ObjectOutputStream out;
    final public ObjectInputStream in;

    public Connection(int port) throws IOException {
        // TODO: Możliwość wybrania innego adresu niż localhost?
        this.ip = InetAddress.getByName("localhost");
        this.socket = new Socket(ip, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Object object) throws IOException {
        this.out.writeObject(object);
        this.socket.getOutputStream().flush();
        this.out.flush();
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return this.in.readObject();
    }

    public void close() throws IOException {
        this.in.close();
        this.out.close();
    }
}
