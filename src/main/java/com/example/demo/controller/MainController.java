
package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainController {

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleShowPatients(ActionEvent event) {
        showAlert("Pacientes", "Aquí se mostrará la gestión de pacientes.");
    }

    @FXML
    private void handleShowUsers(ActionEvent event) {
        showAlert("Usuarios", "Aquí se mostrará la gestión de usuarios.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
