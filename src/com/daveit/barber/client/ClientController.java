package com.daveit.barber.client;

import com.daveit.barber.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ClientController {
    public String name;
    public DatePicker reservationDatePicker;
    public TableView<Reservation> reservationTableView;
    public TableColumn<Reservation, String> hourTableColumn;
    public TableColumn<Reservation, String> statusTableColumn;
    public Button reserveButton;
    public Label statusLabel;
    public Button previousDayButton;
    public Button nextDayButton;
    public ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    ReservationManager reservationManager = new ReservationManager();
    Connection connection;

    @FXML
    void initialize() {
        do {
            name = getCustomerName();
            if(name == null) Platform.exit();
        } while(name != null && name.equals(""));

        reservationTableView.setPlaceholder(new Label("Zawartość dostępna po połączeniu z serwerem"));
        hourTableColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reservationTableView.setItems(reservations);
        reservationDatePicker.setValue(LocalDate.now());

        boldMyReservations();
        setOnSelectionChange();
        (new DataHandler(this)).start();
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

    @FXML
    void reserve() throws IOException {
        Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
        connection.send(new Reservation(reservationDatePicker.getEditor().getText(), reservation.getHour(), false, name));
    }

    void setDisableAll(boolean isDisabled) {
        reservationDatePicker.setDisable(isDisabled);
        previousDayButton.setDisable(isDisabled);
        nextDayButton.setDisable(isDisabled);
        reservationTableView.setDisable(isDisabled);
        reserveButton.setDisable(isDisabled);
    }

    String getCustomerName() {
        TextInputDialog dialog = new TextInputDialog("Imię");
        dialog.setTitle("Salon fryzjerski");
        dialog.setHeaderText("Imię klienta");
        dialog.setContentText("Podaj swoje imię:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    void boldMyReservations() {
        reservationTableView.setRowFactory(rtv -> new TableRow<>() {
            @Override
            public void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getName().equals(name)) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    void setOnSelectionChange() {
        reservationTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            reserveButton.setDisable(current == null || (!current.getName().equals(name) && !current.isFree()));

            if(current == null || !current.isFree())
                reserveButton.setText("Odwołaj");
            else
                reserveButton.setText("Zarezerwuj");
        });
    }
}
