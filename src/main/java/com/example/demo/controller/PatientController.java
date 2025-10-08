package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.Encounter;
import com.example.demo.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PatientController {

    // Patient Fields
    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField dniField;
    @FXML private DatePicker dateField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField genderField;

    // Patient Table
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, String> lastNameColumn;
    @FXML private TableColumn<Patient, String> dniColumn;
    @FXML private TableColumn<Patient, String> dateColumn;
    @FXML private TableColumn<Patient, String> addressColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private TableColumn<Patient, String> genderColumn;

    // Encounter Table
    @FXML private TableView<Encounter> encounterTable;
    @FXML private TableColumn<Encounter, LocalDate> encounterDateColumn;
    @FXML private TableColumn<Encounter, String> encounterReasonColumn;

    // Encounter Details
    @FXML private TextArea diagnosisArea;
    @FXML private TextArea doctorNotesArea;
    @FXML private TextArea treatmentPlanArea;

    private final Database database = new Database();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Encounter> encounterList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Setup Patient Table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        patientTable.setItems(patientList);

        // Setup Encounter Table
        encounterDateColumn.setCellValueFactory(new PropertyValueFactory<>("encounterDate"));
        encounterReasonColumn.setCellValueFactory(new PropertyValueFactory<>("reasonForVisit"));
        encounterTable.setItems(encounterList);

        // Listeners
        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientDetails(newValue));

        encounterTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEncounterDetails(newValue));

        loadPatients();
    }

    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            nameField.setText(patient.getName());
            lastNameField.setText(patient.getLastName());
            dniField.setText(patient.getDni());
            dateField.setValue(LocalDate.parse(patient.getDate()));
            addressField.setText(patient.getAddress());
            phoneField.setText(patient.getPhone());
            genderField.setText(patient.getGender());

            // Load encounters for the selected patient
            loadEncountersForPatient(patient);
        } else {
            clearFields();
        }
    }

    private void showEncounterDetails(Encounter encounter) {
        if (encounter != null) {
            diagnosisArea.setText(encounter.getDiagnosis());
            doctorNotesArea.setText(encounter.getDoctorNotes());
            treatmentPlanArea.setText(encounter.getTreatmentPlan());
        } else {
            clearEncounterDetails();
        }
    }

    private void loadPatients() {
        patientList.clear();
        String url = "jdbc:sqlite:src/main/resources/database/database.db";
        String sql = "SELECT * FROM patients";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("dni"),
                        rs.getString("date"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("gender")
                );
                patient.setId(rs.getInt("id")); // IMPORTANT: Set the ID
                patientList.add(patient);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadEncountersForPatient(Patient patient) {
        encounterList.clear();
        if (patient != null) {
            encounterList.addAll(database.getEncountersForPatient(patient.getId()));
        }
        clearEncounterDetails();
    }

    @FXML
    private void addPatient() {
        Patient patient = new Patient(
                nameField.getText(),
                lastNameField.getText(),
                dniField.getText(),
                dateField.getValue().toString(),
                addressField.getText(),
                phoneField.getText(),
                genderField.getText()
        );
        database.insert(patient);
        loadPatients(); // Reload the patient list
        clearFields();
    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            database.deletePatient(selectedPatient.getDni());
            loadPatients(); // Reload the patient list
        }
    }

    @FXML
    private void openAddEncounterWindow() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un paciente primero.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddEncounter.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Añadir Nueva Visita para " + selectedPatient.getName());
            stage.setScene(new Scene(loader.load()));

            AddEncounterController controller = loader.getController();
            controller.setPatient(selectedPatient, this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshEncounterTable() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        loadEncountersForPatient(selectedPatient);
    }

    private void clearFields() {
        nameField.clear();
        lastNameField.clear();
        dniField.clear();
        dateField.setValue(null);
        addressField.clear();
        phoneField.clear();
        genderField.clear();
        encounterList.clear();
        clearEncounterDetails();
    }

    private void clearEncounterDetails() {
        diagnosisArea.clear();
        doctorNotesArea.clear();
        treatmentPlanArea.clear();
    }
}
