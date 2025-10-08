package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.Encounter;
import com.example.demo.model.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddEncounterController {

    @FXML
    private DatePicker encounterDateField;
    @FXML
    private TextField reasonField;
    @FXML
    private TextArea diagnosisArea;
    @FXML
    private TextArea doctorNotesArea;
    @FXML
    private TextArea treatmentPlanArea;

    private Patient selectedPatient;
    private Database database;
    private PatientController patientController; // To refresh the table

    public void initialize() {
        database = new Database();
        encounterDateField.setValue(LocalDate.now()); // Default to today's date
    }

    // Method to pass the selected patient from the main controller
    public void setPatient(Patient patient, PatientController controller) {
        this.selectedPatient = patient;
        this.patientController = controller;
    }

    @FXML
    private void addEncounter() {
        if (selectedPatient == null) {
            // Show an error message or handle
            System.out.println("No patient selected!");
            return;
        }

        LocalDate encounterDate = encounterDateField.getValue();
        String reason = reasonField.getText();
        String diagnosis = diagnosisArea.getText();
        String doctorNotes = doctorNotesArea.getText();
        String treatmentPlan = treatmentPlanArea.getText();

        Encounter newEncounter = new Encounter(
                selectedPatient.getId(),
                encounterDate,
                reason,
                diagnosis,
                doctorNotes,
                treatmentPlan
        );

        database.insertEncounter(newEncounter);

        // Refresh the encounter table in the main view
        patientController.refreshEncounterTable();

        // Close the window
        Stage stage = (Stage) reasonField.getScene().getWindow();
        stage.close();
    }
}
