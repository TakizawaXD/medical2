package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.service.LoginService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;
    
    @FXML
    private JFXButton registerButton; // This should match the fx:id in the FXML

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @FXML
    void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username and password are required.");
            return;
        }

        boolean authenticated = loginService.authenticate(username, password);

        if (authenticated) {
            Platform.runLater(() -> {
                try {
                    // Switch to the main scene
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
                    ConfigurableApplicationContext context = DemoApplication.getContext();
                    loader.setControllerFactory(context::getBean);
                    Parent root = loader.load();
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            // Show error alert on the JavaFX Application Thread
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid username or password."));
        }
    }

    @FXML
    void showRegister() {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            ConfigurableApplicationContext context = DemoApplication.getContext();
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
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
