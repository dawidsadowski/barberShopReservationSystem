package com.daveit.barber.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    ClientController clientController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = loader.load();
        clientController = loader.getController();
        primaryStage.setTitle("Salon fryzjerski");
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber16.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber24.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/barber32.png")));
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        if(clientController.connection != null) {
            clientController.connection.socket.close();
            clientController.connection.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
