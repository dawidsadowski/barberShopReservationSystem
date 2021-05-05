package com.daveit.barber.server;

import com.daveit.barber.Reservation;
import com.daveit.barber.ReservationManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.time.LocalDate;

public class ServerController {
    public ServerConnection serverConnection;
    public final File file = new File("reservation_manager.obj");
    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;
    public ReservationManager reservationManager;
    public ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    public DatePicker reservationDatePicker;
    public TableView<Reservation> reservationTableView;
    public TableColumn<Reservation, String> hourTableColumn;
    public TableColumn<Reservation, String> statusTableColumn;
    public TableColumn<Reservation, String> nameTableColumn;
    public Button previousDayButton;
    public Button nextDayButton;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        reservationDatePicker.setValue(LocalDate.now());
        hourTableColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        if(file.createNewFile()) {
            reservationManager = new ReservationManager();
        } else {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            reservationManager = (ReservationManager)objectInputStream.readObject();
        }

        objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        checkReservations();

        // TODO: Możliwość wybrania innego portu
        serverConnection = new ServerConnection(3000);

        (new Thread(() -> {
            try {
                System.out.println("Connection started.");
                serverConnection.acceptClients(this);
            } catch (IOException ioException) {
                System.out.println("Connection closed.");
            }
        })).start();
    }

    @FXML
    void setPreviousDay() {
        LocalDate date = reservationDatePicker.getValue();
        reservationDatePicker.setValue(date.minusDays(1));
        checkReservations();
    }

    @FXML
    void setNextDay() {
        LocalDate date = reservationDatePicker.getValue();
        reservationDatePicker.setValue(date.plusDays(1));
        checkReservations();
    }

    @FXML
    void checkReservations() {
        Platform.runLater(() -> {
            reservations = reservationManager.getReservationList(reservationDatePicker.getEditor().getText());
            reservationTableView.setItems(reservations);
        });
    }
}
