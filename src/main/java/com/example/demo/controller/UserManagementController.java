package com.example.demo.controller;

import com.example.demo.data.Database;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserManagementController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private Database database;

    public UserManagementController() {
        database = new Database();
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole().getName()));

        // Disable edit and delete buttons if no user is selected
        editButton.disableProperty().bind(userTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(userTable.getSelectionModel().selectedItemProperty().isNull());

        loadUsers();
    }

    private void loadUsers() {
        List<User> userList = database.getAllUsers();
        ObservableList<User> observableUserList = FXCollections.observableArrayList(userList);
        userTable.setItems(observableUserList);
    }

    @FXML
    private void handle_add_user() {
        try {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Añadir Usuario");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/demo/view/AddUserDialog.fxml"));
            dialog.setDialogPane(fxmlLoader.load());

            TextField usernameField = (TextField) dialog.getDialogPane().lookup("#usernameField");
            PasswordField passwordField = (PasswordField) dialog.getDialogPane().lookup("#passwordField");
            ComboBox<Role> roleComboBox = (ComboBox<Role>) dialog.getDialogPane().lookup("#roleComboBox");

            roleComboBox.setItems(FXCollections.observableArrayList(database.getRoles()));
            roleComboBox.setConverter(new StringConverter<Role>() {
                @Override
                public String toString(Role object) { return object != null ? object.getName() : ""; }
                @Override
                public Role fromString(String string) { return null; }
            });

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            Runnable updateOkButtonState = () -> {
                boolean disable = usernameField.getText().trim().isEmpty() ||
                                  passwordField.getText().isEmpty() ||
                                  roleComboBox.getSelectionModel().isEmpty();
                okButton.setDisable(disable);
            };

            usernameField.textProperty().addListener((obs, old, aNew) -> updateOkButtonState.run());
            passwordField.textProperty().addListener((obs, old, aNew) -> updateOkButtonState.run());
            roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, old, aNew) -> updateOkButtonState.run());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return new User(usernameField.getText(), passwordField.getText(), roleComboBox.getValue());
                }
                return null;
            });

            Optional<User> result = dialog.showAndWait();

            result.ifPresent(newUser -> {
                database.insertUser(newUser);
                loadUsers();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            return; // Should not happen due to button binding, but as a safeguard
        }

        try {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Editar Usuario");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/demo/view/EditUserDialog.fxml"));
            dialog.setDialogPane(fxmlLoader.load());

            Text usernameText = (Text) dialog.getDialogPane().lookup("#usernameText");
            PasswordField passwordField = (PasswordField) dialog.getDialogPane().lookup("#passwordField");
            ComboBox<Role> roleComboBox = (ComboBox<Role>) dialog.getDialogPane().lookup("#roleComboBox");

            // Pre-populate the fields
            usernameText.setText(selectedUser.getUsername());
            roleComboBox.setItems(FXCollections.observableArrayList(database.getRoles()));
            roleComboBox.setValue(selectedUser.getRole());

            roleComboBox.setConverter(new StringConverter<Role>() {
                @Override
                public String toString(Role object) { return object != null ? object.getName() : ""; }
                @Override
                public Role fromString(String string) { return null; }
            });

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    selectedUser.setRole(roleComboBox.getValue());
                    // Password is only set if the field is not empty
                    if (!passwordField.getText().isEmpty()) {
                        selectedUser.setPassword(passwordField.getText());
                    }
                    return selectedUser;
                }
                return null;
            });

            Optional<User> result = dialog.showAndWait();

            result.ifPresent(editedUser -> {
                boolean passwordChanged = !passwordField.getText().isEmpty();
                database.updateUser(editedUser, passwordChanged);
                loadUsers();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("Eliminar Usuario: " + selectedUser.getUsername());
        alert.setContentText("¿Estás seguro de que deseas eliminar a este usuario? Esta acción es irreversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            database.deleteUser(selectedUser.getUsername());
            loadUsers(); // Refresh the table
        }
    }
}
