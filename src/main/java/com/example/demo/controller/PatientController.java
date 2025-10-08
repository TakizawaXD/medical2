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
import java.time.LocalDate;
import java.time.Period;

public class PatientController {

    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField cedulaField;
    @FXML private TextField emailField;
    @FXML private DatePicker dateField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField genderField;
    @FXML private ComboBox<String> bloodTypeField;
    @FXML private TextField allergiesField;
    @FXML private TextArea medicalHistoryField;
    @FXML private TextField emergencyContactNameField;
    @FXML private TextField emergencyContactPhoneField;
    @FXML private TextField insuranceProviderField;
    @FXML private TextField policyNumberField;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, String> lastNameColumn;
    @FXML private TableColumn<Patient, String> cedulaColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private TableColumn<Patient, String> bloodTypeColumn;
    @FXML private TableColumn<Patient, String> insuranceProviderColumn;
    @FXML private TableView<Encounter> encounterTable;
    @FXML private TableColumn<Encounter, LocalDate> encounterDateColumn;
    @FXML private TableColumn<Encounter, String> encounterReasonColumn;
    @FXML private TextArea diagnosisArea;
    @FXML private TextArea doctorNotesArea;
    @FXML private TextArea treatmentPlanArea;

    private final Database database = new Database();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Encounter> encounterList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupPatientTable();
        setupEncounterTable();
        setupFieldListeners();
        populateBloodTypeComboBox();
        loadPatients();
        ageField.setEditable(false);
    }

    private void setupPatientTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        cedulaColumn.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        insuranceProviderColumn.setCellValueFactory(new PropertyValueFactory<>("insuranceProvider"));
        patientTable.setItems(patientList);
    }

    private void setupEncounterTable() {
        encounterDateColumn.setCellValueFactory(new PropertyValueFactory<>("encounterDate"));
        encounterReasonColumn.setCellValueFactory(new PropertyValueFactory<>("reasonForVisit"));
        encounterTable.setItems(encounterList);
    }

    private void setupFieldListeners() {
        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientDetails(newValue));

        encounterTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEncounterDetails(newValue));

        dateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ageField.setText(String.valueOf(Period.between(newValue, LocalDate.now()).getYears()));
            } else {
                ageField.clear();
            }
        });
    }

    private void populateBloodTypeComboBox() {
        ObservableList<String> bloodTypes = FXCollections.observableArrayList(
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        );
        bloodTypeField.setItems(bloodTypes);
    }

    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            nameField.setText(patient.getName());
            lastNameField.setText(patient.getLastName());
            cedulaField.setText(patient.getCedula());
            emailField.setText(patient.getEmail());
            if (patient.getDate() != null && !patient.getDate().isEmpty()) {
                dateField.setValue(LocalDate.parse(patient.getDate()));
            } else {
                dateField.setValue(null);
            }
            ageField.setText(String.valueOf(patient.getAge()));
            addressField.setText(patient.getAddress());
            phoneField.setText(patient.getPhone());
            genderField.setText(patient.getGender());
            bloodTypeField.setValue(patient.getBloodType());
            allergiesField.setText(patient.getAllergies());
            medicalHistoryField.setText(patient.getMedicalHistory());
            emergencyContactNameField.setText(patient.getEmergencyContactName());
            emergencyContactPhoneField.setText(patient.getEmergencyContactPhone());
            insuranceProviderField.setText(patient.getInsuranceProvider());
            policyNumberField.setText(patient.getPolicyNumber());

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
        patientList.setAll(database.getAllPatients());
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
        if (dateField.getValue() == null) {
            return;
        }
        int age = Period.between(dateField.getValue(), LocalDate.now()).getYears();
        Patient patient = new Patient(
                nameField.getText(), lastNameField.getText(), cedulaField.getText(), emailField.getText(), dateField.getValue().toString(),
                age, addressField.getText(), phoneField.getText(), genderField.getText(), bloodTypeField.getValue(),
                allergiesField.getText(), medicalHistoryField.getText(), emergencyContactNameField.getText(),
                emergencyContactPhoneField.getText(), insuranceProviderField.getText(), policyNumberField.getText()
        );
        database.insert(patient);
        loadPatients();
        clearFields();
    }

    @FXML
    private void updatePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            int age = Period.between(dateField.getValue(), LocalDate.now()).getYears();
            selectedPatient.setName(nameField.getText());
            selectedPatient.setLastName(lastNameField.getText());
            selectedPatient.setCedula(cedulaField.getText());
            selectedPatient.setEmail(emailField.getText());
            selectedPatient.setDate(dateField.getValue().toString());
            selectedPatient.setAge(age);
            selectedPatient.setAddress(addressField.getText());
            selectedPatient.setPhone(phoneField.getText());
            selectedPatient.setGender(genderField.getText());
            selectedPatient.setBloodType(bloodTypeField.getValue());
            selectedPatient.setAllergies(allergiesField.getText());
            selectedPatient.setMedicalHistory(medicalHistoryField.getText());
            selectedPatient.setEmergencyContactName(emergencyContactNameField.getText());
            selectedPatient.setEmergencyContactPhone(emergencyContactPhoneField.getText());
            selectedPatient.setInsuranceProvider(insuranceProviderField.getText());
            selectedPatient.setPolicyNumber(policyNumberField.getText());

            database.updatePatient(selectedPatient);
            loadPatients();
            clearFields();
        }
    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            database.deletePatient(selectedPatient.getCedula());
            loadPatients();
        }
    }

    @FXML
    private void openAddEncounterWindow() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un paciente primero.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/view/AddEncounter.fxml"));
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
        cedulaField.clear();
        emailField.clear();
        dateField.setValue(null);
        ageField.clear();
        addressField.clear();
        phoneField.clear();
        genderField.clear();
        bloodTypeField.setValue(null);
        allergiesField.clear();
        medicalHistoryField.clear();
        emergencyContactNameField.clear();
        emergencyContactPhoneField.clear();
        insuranceProviderField.clear();
        policyNumberField.clear();
        encounterList.clear();
        clearEncounterDetails();
    }

    private void clearEncounterDetails() {
        diagnosisArea.clear();
        doctorNotesArea.clear();
        treatmentPlanArea.clear();
    }
}
