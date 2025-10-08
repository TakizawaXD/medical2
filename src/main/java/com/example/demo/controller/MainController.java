package com.example.demo.controller;

import com.example.demo.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPane;

    private User currentUser;
    private boolean isDarkMode = false;

    public void initData(User user) {
        this.currentUser = user;
        loadDashboardByRole();
    }

    @FXML
    public void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            borderPane.getStylesheets().add(getClass().getResource("/com/example/demo/style/dark-mode.css").toExternalForm());
        } else {
            borderPane.getStylesheets().remove(getClass().getResource("/com/example/demo/style/dark-mode.css").toExternalForm());
        }
    }

    private void loadDashboardByRole() {
        if (currentUser == null || currentUser.getRole() == null) {
            System.err.println("Error: Current user or role is not set.");
            return;
        }

        String roleName = currentUser.getRole().getName();
        String fxmlPath;

        switch (roleName) {
            case "Administrador":
                fxmlPath = "/com/example/demo/view/AdminDashboard.fxml";
                break;
            case "Médico":
                fxmlPath = "/com/example/demo/view/DoctorDashboard.fxml";
                break;
            case "Recepcionista":
                fxmlPath = "/com/example/demo/view/ReceptionistDashboard.fxml";
                break;
            case "Enfermero/a":
                fxmlPath = "/com/example/demo/view/NurseDashboard.fxml";
                break;
            default:
                System.err.println("No dashboard found for role: " + roleName);
                return; // Salir si no hay un rol que coincida
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent dashboardView = loader.load();

            Object controller = loader.getController();
            if (controller instanceof AdminDashboardController) {
                ((AdminDashboardController) controller).setMainController(this);
            } else if (controller instanceof DoctorDashboardController) {
                ((DoctorDashboardController) controller).setMainController(this);
            } else if (controller instanceof ReceptionistDashboardController) {
                ((ReceptionistDashboardController) controller).setMainController(this);
            } else if (controller instanceof NurseDashboardController) {
                ((NurseDashboardController) controller).setMainController(this);
            }

            borderPane.setCenter(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load dashboard: " + fxmlPath);
        }
    }
}
