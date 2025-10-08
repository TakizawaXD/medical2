package com.example.demo.application;

import com.example.demo.data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ejecutar extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database and create tables if they don't exist
        Database database = new Database();
        database.createInitialTables();

        // Load the login view
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/view/Login.fxml"));
        primaryStage.setTitle("Sistema de Gestión Hospitalaria");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
