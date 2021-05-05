package com.daveit.barber.client;

import com.daveit.barber.Connection;
import com.daveit.barber.Reservation;
import com.daveit.barber.ReservationManager;
import javafx.application.Platform;

import java.io.IOException;

public class DataHandler extends Thread {
    ClientController clientController;

    public DataHandler(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        try {
            clientController.connection = new Connection(3000);
            Platform.runLater(() -> clientController.statusLabel.setText("Połączono | Klient: " + clientController.name));
            clientController.reservationManager = (ReservationManager)clientController.connection.receive();
            clientController.checkReservations();
            clientController.setDisableAll(false);
            clientController.reserveButton.setDisable(true);

            while(true) {
                Reservation reservation = (Reservation)clientController.connection.receive();

                if(reservation.isFree()) {
                    clientController.reservationManager.removeReservation(reservation.getKey());
                } else {
                    clientController.reservationManager.addReservation(reservation.getDate(), reservation.getHour(), reservation.getName());
                }
                clientController.checkReservations();
            }
        } catch (IOException | ClassNotFoundException ioException) {
            clientController.setDisableAll(true);
            Platform.runLater(() -> clientController.statusLabel.setText("Serwer niedostępny ¯\\_(ツ)_/¯"));
        }
    }
}
