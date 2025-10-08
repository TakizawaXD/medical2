package com.example.demo.controller;

import com.example.demo.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPane;

    private User currentUser;

    public void initData(User user) {
        this.currentUser = user;
        setupDashboard();
    }

    private void setupDashboard() {
        VBox navigationBox = new VBox();
        navigationBox.setSpacing(10);
        navigationBox.setStyle("-fx-padding: 10;");

        if (currentUser == null || currentUser.getRole() == null) {
            System.out.println("Error: Current user or role is not set.");
            return;
        }

        String roleName = currentUser.getRole().getName();

        switch (roleName) {
            case "Administrador":
                navigationBox.getChildren().add(createButton("Gestionar Pacientes", this::showPatients));
                navigationBox.getChildren().add(createButton("Gestionar Usuarios", this::showUsers));
                navigationBox.getChildren().add(createButton("Ver Reportes", this::showReports));
                break;
            case "Médico":
                navigationBox.getChildren().add(createButton("Mi Agenda", this::showAgenda));
                navigationBox.getChildren().add(createButton("Buscar Paciente", this::showPatients));
                break;
            case "Recepcionista":
                navigationBox.getChildren().add(createButton("Registrar Paciente", this::showPatients));
                navigationBox.getChildren().add(createButton("Agendar Cita", this::showAppointments));
                break;
            default:
                // No options for unknown roles
                break;
        }

        borderPane.setLeft(navigationBox);
    }

    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> action.run());
        return button;
    }

    private void loadView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPatients() {
        loadView("/com/example/demo/view/Patient.fxml");
    }

    @FXML
    private void showUsers() {
        loadView("/com/example/demo/view/UserManagement.fxml");
    }

    private void showReports() {
        // Placeholder
        System.out.println("Navigating to Reports...");
    }

    private void showAgenda() {
        // Placeholder
        System.out.println("Navigating to Doctor's Agenda...");
    }

    private void showAppointments() {
        // Placeholder
        System.out.println("Navigating to Appointment Scheduling...");
    }
}
