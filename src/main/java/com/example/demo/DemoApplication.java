package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication extends Application {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        context = SpringApplication.run(DemoApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/view/Login.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        primaryStage.setTitle("Medical App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
