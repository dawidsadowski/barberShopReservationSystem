package com.daveit.barber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.HashMap;

public class ReservationManager implements Serializable {
    final HashMap<String, Reservation> reservations;

    public ReservationManager() {
        this.reservations = new HashMap<>();
    }

    public Reservation getReservation(String dateTime) {
        return reservations.get(dateTime);
    }

    public void addReservation(String date, String hour, String name) {
        String key = date + " " + hour;
        reservations.put(key, new Reservation(date, hour, false, name)); // it doesn't make sense
    }

    public void removeReservation(String key) {
        reservations.remove(key);
    }

    public ObservableList<Reservation> getReservationList(String date) {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();

        for(int i = 10; i <= 17; i++) {
            String hour = i + ":00";
            String key = date + " " + hour;

            Reservation reservation = this.reservations.get(key);

            if(reservation == null) {
                reservations.add(new Reservation(date, hour, true, ""));
            } else {
                reservations.add(new Reservation(date, hour, false, reservation.getName()));
            }
        }

        return reservations;
    }
}
