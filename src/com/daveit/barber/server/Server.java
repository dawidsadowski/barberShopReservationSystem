package com.daveit.barber.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Optional;

public class Server extends Application {
    public static HashMap<Socket, ClientHandler> clients = new HashMap<>();
    ServerController serverController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server.fxml"));
        Parent root = loader.load();
        serverController = loader.getController();
        primaryStage.setTitle("Salon fryzjerski - Serwer");
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber16.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber24.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber32.png")));
        primaryStage.setOnCloseRequest(this::showExitConfirmation);
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        serverController.objectOutputStream.writeObject(serverController.reservationManager);
        serverController.serverConnection.close();

        clients.forEach((socket, clientHandler) -> {
            try {
                socket.close();
                clientHandler.kill();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    void showExitConfirmation(WindowEvent windowEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zako??czenie aplikacji");
        alert.setHeaderText((clients.size() > 0 ? "Do serwera s?? pod????czeni klienci.\nZako??czenie dzia??ania aplikacji mo??e wi??za?? si?? z utrat?? danych." : "Po zako??czeniu dzia??ania aplikacji klienci nie b??d?? mogli rejestrowa?? wizyt."));
        alert.setContentText("Czy na pewno chcesz zako??czy?? dzia??anie aplikacji?");

        ButtonType yesButton = new ButtonType("Tak", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType noButton = new ButtonType("Nie", ButtonBar.ButtonData.OK_DONE);

        alert.getButtonTypes().set(0, yesButton);
        alert.getButtonTypes().set(1, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Platform.exit();
        } else {
            windowEvent.consume();
        }
    }
}
