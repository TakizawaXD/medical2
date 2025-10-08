package com.example.demo.controller;

import com.example.demo.DemoApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    void showPatients() throws IOException {
        loadView("/fxml/Patient.fxml");
    }

    @FXML
    void showUsers() {
        // TODO: Load users view
    }

    private void loadView(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        ConfigurableApplicationContext context = DemoApplication.getContext();
        loader.setControllerFactory(context::getBean);
        Parent view = loader.load();
        borderPane.setCenter(view);
    }

}
