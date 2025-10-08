package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Database database;

    public LoginController() {
        database = new Database();
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = database.getUser(username, password);

        if (user != null) {
            // Login successful
            String welcomeMessage = String.format("¡Bienvenido, %s! (%s)", user.getUsername(), user.getRole().getName());
            showAlert(Alert.AlertType.INFORMATION, "Inicio de Sesión Exitoso", welcomeMessage);
            loadMainApplication(user);
        } else {
            // Login failed
            showAlert(Alert.AlertType.ERROR, "Error de Inicio de Sesión", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void showRegister() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/view/Register.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMainApplication(User user) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/view/Main.fxml"));
            Parent root = loader.load();

            // Pass the user object to the MainController
            MainController mainController = loader.getController();
            mainController.initData(user);

            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Gestión Hospitalaria - Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
