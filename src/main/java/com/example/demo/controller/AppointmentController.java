package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, LocalDateTime> timeColumn;

    @FXML
    private TableColumn<Appointment, String> patientColumn;

    @FXML
    private TableColumn<Appointment, String> doctorColumn;

    @FXML
    private TableColumn<Appointment, String> reasonColumn;

    private final Database database = new Database();
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        // Custom cell factory to display only the time part
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(timeFormatter));
                }
            }
        });


        patientColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPatient().toString()));
        doctorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDoctor().toString()));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        appointmentTable.setItems(appointmentList);

        datePicker.setValue(LocalDate.now());
        loadAppointments();

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadAppointments();
            }
        });
    }

    private void loadAppointments() {
        appointmentList.clear();
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            appointmentList.addAll(database.getAppointmentsForDate(selectedDate));
        }
    }

    @FXML
    private void addAppointment() {
        try {
            Dialog<Appointment> dialog = createAppointmentDialog("Añadir Nueva Cita", null);
            Optional<Appointment> result = dialog.showAndWait();
            result.ifPresent(appointment -> {
                database.insertAppointment(appointment);
                loadAppointments();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editAppointment() {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("Ninguna cita seleccionada", "Por favor, seleccione una cita para editar.");
            return;
        }

        try {
            Dialog<Appointment> dialog = createAppointmentDialog("Editar Cita", selectedAppointment);
            Optional<Appointment> result = dialog.showAndWait();
            result.ifPresent(appointment -> {
                database.updateAppointment(appointment);
                loadAppointments();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAppointment() {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            database.deleteAppointment(selectedAppointment.getId());
            loadAppointments();
        } else {
            showAlert("Ninguna cita seleccionada", "Por favor, seleccione una cita para eliminar.");
        }
    }

    private Dialog<Appointment> createAppointmentDialog(String title, Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/view/AddAppointmentDialog.fxml"));
        DialogPane dialogPane = loader.load();

        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        ComboBox<Patient> patientComboBox = (ComboBox<Patient>) dialog.getDialogPane().lookup("#patientComboBox");
        ComboBox<User> doctorComboBox = (ComboBox<User>) dialog.getDialogPane().lookup("#doctorComboBox");
        DatePicker datePicker = (DatePicker) dialog.getDialogPane().lookup("#datePicker");
        TextField timeField = (TextField) dialog.getDialogPane().lookup("#timeField");
        TextField reasonField = (TextField) dialog.getDialogPane().lookup("#reasonField");

        // Populate combo boxes
        patientComboBox.setItems(FXCollections.observableArrayList(database.getAllPatients()));
        List<User> allUsers = database.getAllUsers();
        List<User> doctors = allUsers.stream()
                .filter(user -> "Médico".equals(user.getRole().getName()))
                .collect(Collectors.toList());
        doctorComboBox.setItems(FXCollections.observableArrayList(doctors));


        // Set initial values if editing
        if (appointment != null) {
            patientComboBox.setValue(appointment.getPatient());
            doctorComboBox.setValue(appointment.getDoctor());
            datePicker.setValue(appointment.getAppointmentDateTime().toLocalDate());
            timeField.setText(appointment.getAppointmentDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            reasonField.setText(appointment.getReason());
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Patient patient = patientComboBox.getValue();
                User doctor = doctorComboBox.getValue();
                LocalDate date = datePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                String reason = reasonField.getText();
                LocalDateTime dateTime = LocalDateTime.of(date, time);

                if (appointment == null) {
                    // Creating a new appointment
                    return new Appointment(0, patient, doctor, dateTime, reason);
                } else {
                    // Updating an existing appointment
                    return new Appointment(appointment.getId(), patient, doctor, dateTime, reason);
                }
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
