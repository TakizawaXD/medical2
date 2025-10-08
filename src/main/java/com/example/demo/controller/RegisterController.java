package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private Database database;

    public RegisterController() {
        database = new Database();
    }

    @FXML
    public void initialize() {
        List<Role> roles = database.getRoles();
        roleComboBox.getItems().addAll(roles);
    }

    @FXML
    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        Role selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Error en el Registro", "Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error en el Registro", "Las contraseñas no coinciden.");
            return;
        }

        User newUser = new User(username, password, selectedRole);
        database.insertUser(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "¡Usuario registrado correctamente!");
        backToLogin();
    }

    @FXML
    private void backToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/view/Login.fxml"));
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
