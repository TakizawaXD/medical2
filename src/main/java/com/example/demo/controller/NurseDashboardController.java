package com.example.demo.controller;

import com.example.demo.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class NurseDashboardController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton darkModeToggle;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void toggleDarkMode() {
        if (mainController != null) {
            mainController.toggleDarkMode();
        }
    }

    @FXML
    private void showPatientManagement() {
        loadView("/com/example/demo/view/Patient.fxml");
    }

    @FXML
    private void showAppointments() {
        loadView("/com/example/demo/view/Appointment.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
