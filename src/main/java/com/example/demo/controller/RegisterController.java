package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import okhttp3.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RegisterController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXPasswordField confirmPasswordField;

    @FXML
    private JFXComboBox<String> roleComboBox;

    @FXML
    private JFXButton registerButton;

    @FXML
    private JFXButton backToLoginButton;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("ADMIN", "MEDICO", "ENFERMERO", "LABORATORIO", "FACTURACION");
    }

    @FXML
    void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            return;
        }

        // Create user map
        Map<String, String> user = Map.of(
            "username", username,
            "password", password,
            "role", role
        );

        String json = gson.toJson(user);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/auth/register")
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Connection Error", "Failed to connect to the server."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You can now log in.");
                        backToLogin();
                    });
                } else if (response.code() == 409) { // Conflict
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists."));
                } else {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Registration Failed", "An unknown error occurred."));
                }
            }
        });
    }

    @FXML
    void backToLogin() {
        try {
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
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
