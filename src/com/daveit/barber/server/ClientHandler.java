package com.daveit.barber.server;

import com.daveit.barber.Connection;
import com.daveit.barber.Reservation;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {
    Connection connection;
    ServerController serverController;
    boolean alive = true;

    public ClientHandler(Connection connection, ServerController serverController) {
        this.connection = connection;
        this.serverController = serverController;
    }

    public void kill() {
        alive = false;
    }

    @Override
    public void run() {
        System.out.println("Client joined");

        try {
            connection.send(serverController.reservationManager);

            while(alive) {
                Reservation receivedReservation = (Reservation) connection.receive();
                Reservation reservation = serverController.reservationManager.getReservation(receivedReservation.getKey());

                if(reservation == null) {
                    System.out.println("Reserved on " + receivedReservation.getKey() + " for " + receivedReservation.getName());
                    serverController.reservationManager.addReservation(receivedReservation.getDate(), receivedReservation.getHour(), receivedReservation.getName());
                } else {
                    if(reservation.equals(receivedReservation)) {
                        receivedReservation.setFree(true);
                        reservation.setFree(true);
                    }
                }

                sendReservationToClients(receivedReservation);
                if(receivedReservation.isFree()) serverController.reservationManager.removeReservation(receivedReservation.getKey());
                serverController.checkReservations();
            }
        } catch (IOException | ClassNotFoundException ioException) {
            Server.clients.remove(connection.socket);
            System.out.println("Client " + connection.socket + " disconnected.");
        }
    }

    void sendReservationToClients(Reservation reservation) throws IOException {
        for(Map.Entry<Socket, ClientHandler> entry : Server.clients.entrySet()) {
            ClientHandler client = entry.getValue();
            client.connection.send(reservation);
            System.out.println("Reservation sent to: " + entry.getKey() + "");
        }
    }
}
