package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Long> idColumn;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> lastnameColumn;

    @FXML
    private TableColumn<Patient, Integer> ageColumn;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField lastnameField;

    @FXML
    private JFXTextField ageField;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton deleteButton;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        loadPatients();
    }

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(patientRepository.findAll());
        patientTable.setItems(patientList);
    }

    @FXML
    private void addPatient() {
        Patient patient = new Patient(nameField.getText(), lastnameField.getText(), Integer.parseInt(ageField.getText()));
        patientRepository.save(patient);
        loadPatients();
        clearFields();
    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            patientRepository.delete(selectedPatient);
            loadPatients();
        }
    }

    private void clearFields() {
        nameField.clear();
        lastnameField.clear();
        ageField.clear();
    }
}
