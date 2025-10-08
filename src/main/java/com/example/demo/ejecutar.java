<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<AnchorPane prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/17" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.demo.AddEncounterController">
    <children>
        <VBox alignment="CENTER" layoutX="14.0" layoutY="14.0" prefHeight="372.0" prefWidth="572.0" spacing="10.0">
            <children>
                <Label text="Añadir Nueva Visita Médica" style="-fx-font-size: 18px; -fx-font-weight: bold;" />
                <GridPane hgap="10.0" vgap="10.0">
                    <columnConstraints>
                        <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="150.0" />
                        <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="400.0" />
                    </columnConstraints>
                    <rowConstraints>
                        <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES" />
                        <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES" />
                        <RowConstraints minHeight="10.0" prefHeight="60.0" vgrow="SOMETIMES" />
                        <RowConstraints minHeight="10.0" prefHeight="80.0" vgrow="SOMETIMES" />
                        <RowConstraints minHeight="10.0" prefHeight="60.0" vgrow="SOMETIMES" />
                    </rowConstraints>
                    <children>
                        <Label text="Fecha de la Visita:" GridPane.rowIndex="0" />
                        <DatePicker fx:id="encounterDateField" GridPane.columnIndex="1" GridPane.rowIndex="0" />
                        <Label text="Motivo de la Visita:" GridPane.rowIndex="1" />
                        <TextField fx:id="reasonField" GridPane.columnIndex="1" GridPane.rowIndex="1" />
                        <Label text="Diagnóstico:" GridPane.rowIndex="2" />
                        <TextArea fx:id="diagnosisArea" prefHeight="60.0" prefWidth="400.0" GridPane.columnIndex="1" GridPane.rowIndex="2" />
                        <Label text="Notas del Médico:" GridPane.rowIndex="3" />
                        <TextArea fx:id="
package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ejecutar extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database and create tables if they don't exist
        Database database = new Database();
        database.createInitialTables();

        // Load the login view
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        primaryStage.setTitle("Sistema de Gestión Hospitalaria");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
